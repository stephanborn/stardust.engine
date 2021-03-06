/*******************************************************************************
 * Copyright (c) 2011 SunGard CSA LLC and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    SunGard CSA LLC - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.stardust.engine.core.pojo.data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import org.eclipse.stardust.common.Assert;
import org.eclipse.stardust.common.CollectionUtils;
import org.eclipse.stardust.common.Direction;
import org.eclipse.stardust.common.StringUtils;
import org.eclipse.stardust.common.error.InternalException;
import org.eclipse.stardust.common.error.InvalidValueException;
import org.eclipse.stardust.common.error.PublicException;
import org.eclipse.stardust.common.log.LogManager;
import org.eclipse.stardust.common.log.Logger;
import org.eclipse.stardust.common.reflect.Reflect;
import org.eclipse.stardust.common.reflect.ResolvedMethod;
import org.eclipse.stardust.engine.api.model.PredefinedConstants;
import org.eclipse.stardust.engine.core.model.utils.ModelElement;
import org.eclipse.stardust.engine.core.pojo.utils.JavaAccessPointType;
import org.eclipse.stardust.engine.core.spi.extensions.model.AccessPoint;
import org.eclipse.stardust.engine.core.spi.extensions.model.BridgeObject;
import org.eclipse.stardust.engine.extensions.ejb.data.EntityBeanConstants;


/**
 * @author rsauer
 * @version $Revision$
 */
public class JavaDataTypeUtils
{
   private static final Logger trace = LogManager.getLogger(JavaDataTypeUtils.class);

   private static final String PROTOCOL_SEPARATOR = "://";
   private static final String PATH_SEPARATOR = ".";
   
   private static final String CACHED_REFERENCE_CLASS = JavaDataTypeUtils.class.getName()
         + ".CachedReferenceClass";

   public static final String METHOD_CACHE = JavaDataTypeUtils.class.getName()
         + ".MethodCache";

   public static Map initPrimitiveAttributes(Type type, String defaultValue)
   {
      Map attributes = new HashMap();

      attributes.put(PredefinedConstants.TYPE_ATT, type);
      if (null != defaultValue)
      {
         attributes.put(PredefinedConstants.DEFAULT_VALUE_ATT, defaultValue);
      }

      return attributes;
   }

   public static Map initSerializableBeanAttributes(String beanClassName)
   {
      Map attributes = new HashMap();

      attributes.put(PredefinedConstants.CLASS_NAME_ATT, beanClassName);

      return attributes;
   }

   public static String getReferenceClassName(AccessPoint data)
   {
      if (data.getType().getId().equals(PredefinedConstants.PRIMITIVE_DATA))
      {
         Type type = (Type) data.getAttribute(PredefinedConstants.TYPE_ATT);
         return Reflect.getClassFromAbbreviatedName(type.getName()).getName();
      }
      else if (data.getType().getId().equals(PredefinedConstants.SERIALIZABLE_DATA))
      {
         return (String) data.getAttribute(PredefinedConstants.CLASS_NAME_ATT);
      }
      else if (data.getType().getId().equals(PredefinedConstants.ENTITY_BEAN_DATA))
      {
         boolean is3x = EntityBeanConstants.VERSION_3_X.equals(
               data.getAttribute(EntityBeanConstants.VERSION_ATT));
         return (String) data.getAttribute(is3x ?
               PredefinedConstants.CLASS_NAME_ATT : PredefinedConstants.REMOTE_INTERFACE_ATT);
      }
      else
      {
         throw new PublicException("Not a Java data type.");
      }
   }

   public static Class getReferenceClass(AccessPoint data)
   {
      Class referenceClass = (data instanceof ModelElement)
            ? (Class) ((ModelElement) data).getRuntimeAttribute(CACHED_REFERENCE_CLASS)
            : null;
      if (null == referenceClass)
      {
         final String referenceClassName = getReferenceClassName(data);
         
         if ( !StringUtils.isEmpty(referenceClassName))
         {
            referenceClass = Reflect.getClassFromAbbreviatedName(referenceClassName);
            
            if ((null != referenceClass) && (data instanceof ModelElement))
            {
               ((ModelElement) data).setRuntimeAttribute(CACHED_REFERENCE_CLASS, referenceClass);
            }
         }
      }
      
      return referenceClass;
   }

   public static AccessPoint createIntrinsicAccessPoint(ModelElement parent, String id,
         String name, String clazz, Direction direction, boolean browsable,
         Object characteristics)
   {
      JavaAccessPoint result = new JavaAccessPoint(id, name, direction);
      result.setAttribute(PredefinedConstants.CLASS_NAME_ATT, clazz);
      if (characteristics != null)
      {
         result.setAttribute(PredefinedConstants.FLAVOR_ATT, characteristics);
      }
      result.setAttribute(PredefinedConstants.BROWSABLE_ATT,
            browsable ? Boolean.TRUE : Boolean.FALSE);

      result.setParent(parent);

      return result;
   }

   public static AccessPoint createIntrinsicAccessPoint(String id, String name,
         String clazz, Direction direction, boolean browsable, Object characteristics)
   {
      return createIntrinsicAccessPoint(null, id, name, clazz, direction, browsable,
            characteristics);
   }

   public static List parse(String pathExpression)
   {
      List parsedPath = Collections.EMPTY_LIST;

      if (!StringUtils.isEmpty(pathExpression))
      {
         int protocolIndex = pathExpression.indexOf(PROTOCOL_SEPARATOR);
         if (protocolIndex != -1)
         {
            String protocol = pathExpression.substring(0, protocolIndex);
            if (!"java".equals(protocol))
            {
               throw new PublicException("Invalid Java Bean access path type: "
                     + pathExpression);
            }
            pathExpression = pathExpression.substring(
                  protocolIndex + PROTOCOL_SEPARATOR.length());
         }

         Iterator pathTokens = StringUtils.split(pathExpression, PATH_SEPARATOR);

         parsedPath = new ArrayList();

         StringBuffer buffer = new StringBuffer();

         while (pathTokens.hasNext())
         {
            String token = (String) pathTokens.next();
            buffer.append(token);
            if (token.endsWith(")"))
            {
               parsedPath.add(buffer.toString());
               buffer = new StringBuffer();
            }
            else
            {
               buffer.append(".");
            }
         }
         // @todo (ub): if the buffer is != "" here, it is an error
      }
      return parsedPath;
   }

   public static BridgeObject getBridgeObject(AccessPoint point, String path)
   {
      Class accessPointType = getReferenceClass(point);
      Direction direction = null;
      Class currentType = accessPointType;
      if (!StringUtils.isEmpty(path))
      {
         direction = Direction.OUT;
         for (Iterator i = parse(path).iterator(); i.hasNext();)
         {
            String element = (String) i.next();
            try
            {
               if (!i.hasNext() && !element.endsWith("()"))
               {
                  //we have found a setter here
                  currentType = Reflect.decodeMethod(currentType, element)
                        .getParameterTypes()[0];
                  direction = Direction.IN;
                  // todo: (france, fh) shouldn't be the setter the endpoint ???
               }
               else
               {
                  currentType = Reflect.decodeMethod(currentType, element).getReturnType();
               }
            }
            catch (InternalException x)
            {
               // @todo (france, ub): should we throw a public exception instead?!
               throw new InternalException(
                     "Method '" + element + "' not available or not accessible.", x);
            }
            if (currentType.isPrimitive())
            {
               currentType = Reflect.getWrapperClassFromPrimitiveClassName(currentType);
            }
         }
      }

      return new BridgeObject(currentType, direction);
   }

   public static Object evaluate(String outPath, Object accessPoint)
         throws InvocationTargetException
   {
      Object value = accessPoint;

      List parsedPath = (null != value) ? parse(outPath) : Collections.EMPTY_LIST;

      if (!parsedPath.isEmpty())
      {
         Iterator i = parsedPath.iterator();

         while ((null != value) && i.hasNext())
         {
            String element = (String) i.next();

            try
            {
               Method method = Reflect.decodeMethod(value.getClass(), element);

               value = method.invoke(value);
            }
            catch (InvocationTargetException x)
            {
               trace.debug("Failed evaluating Java OUT path.", x.getTargetException());
               throw x;
            }
            catch (PublicException x)
            {
               throw x;
            }
            catch (InternalException x)
            {
               throw new PublicException("The method " + element + " from class "
                     + value.getClass() + " does not exist or is not accessible.");
            }
            catch (IllegalAccessException x)
            {
               trace.debug("", x);
               throw new PublicException("The method " + element
                     + " is not accessible in class " + value.getClass() + ".");
            }
            catch (IllegalArgumentException x)
            {
               trace.debug("", x);
               throw new PublicException("Illegal argument for method " + element
                     + " in class " + value.getClass() + ".");
            }
            catch (Exception x)
            {
               throw new PublicException(x);
            }
         }

         if (trace.isDebugEnabled())
         {
            if (!parsedPath.isEmpty())
            {
               trace.debug("Value of " + accessPoint + "." + outPath +  " is: '"
                     + value + "'");
            }
            else
            {
               trace.debug("Value of " + accessPoint + " is: '" + value + "'");
            }
         }
      }
      return value;
   }

   public static Object evaluateGetter(AccessPoint apGetter, Object instance)
         throws InvocationTargetException
   {
      Assert.condition(JavaAccessPointType.METHOD == apGetter.getAttribute(PredefinedConstants.FLAVOR_ATT));
      Assert.condition(Direction.OUT == apGetter.getDirection());
      
      ResolvedMethod getter = null;
      
      if (apGetter instanceof ModelElement)
      {
         Map methodCache = (Map) ((ModelElement) apGetter).getRuntimeAttribute(METHOD_CACHE);
         
         getter = (null != methodCache)
         ? (ResolvedMethod) methodCache.get(instance.getClass())
               : null;
         
         if (null == getter)
         {
            List parsedPath = parse(apGetter.getId());
            
            if (1 == parsedPath.size())
            {
               Method mthdGetter = Reflect.decodeMethod(instance.getClass(),
                     (String) parsedPath.get(0));
               if (null != mthdGetter)
               {
                  getter = new ResolvedMethod(mthdGetter);
                  
                  if ((null == methodCache) || methodCache.isEmpty())
                  {
                     methodCache = Collections.singletonMap(instance.getClass(), getter);
                  }
                  else
                  {
                     methodCache = CollectionUtils.copyMap(methodCache);
                     methodCache.put(instance.getClass(), getter);
                  }
                  
                  ((ModelElement) apGetter).setRuntimeAttribute(METHOD_CACHE, methodCache);
               }
            }
         }
      }
      
      Object value = null;
      if ((null != instance) && (null != getter))
      {
         try
         {
            value = evaluateGetter(getter.self, instance);
         }
         catch (InvocationTargetException ite)
         {
            trace.debug("Failed evaluating Java OUT path.", ite.getTargetException());
            throw ite;
         }
      }

      return value;
   }
   
   public static Object evaluateGetter(Method mthdGetter, Object instance)
         throws InvocationTargetException
   {
      Object result = null;

      if ((null != instance) && (null != mthdGetter))
      {
         try
         {
            result = mthdGetter.invoke(instance);
         }
         catch (IllegalAccessException iae)
         {
            trace.debug("", iae);
            throw new InvocationTargetException(iae, "The method " + mthdGetter.getName()
                  + " is not accessible in class " + instance.getClass() + ".");
         }
         catch (IllegalArgumentException iae)
         {
            trace.debug("", iae);
            throw new InvocationTargetException(iae, "Illegal argument for method "
                  + mthdGetter.getName() + " in class " + instance.getClass() + ".");
         }
         catch (InvocationTargetException ite)
         {
            trace.debug("Failed evaluating Java OUT path.", ite.getTargetException());
            throw ite;
         }
      }

      if (trace.isDebugEnabled())
      {
         trace.debug("Value of " + instance + "." + mthdGetter.getName() + " is: '"
               + result + "'");
      }
      
      return result;
   }

   public static Object evaluateSetter(AccessPoint apSetter, Object instance, Object value)
         throws InvocationTargetException
   {
      Assert.condition(JavaAccessPointType.METHOD == apSetter.getAttribute(PredefinedConstants.FLAVOR_ATT));
      Assert.condition(Direction.IN == apSetter.getDirection());

      ResolvedMethod setter = null;

      if (apSetter instanceof ModelElement)
      {
         Map methodCache = (Map) ((ModelElement) apSetter).getRuntimeAttribute(METHOD_CACHE);
         
         setter = (null != methodCache)
               ? (ResolvedMethod) methodCache.get(instance.getClass())
               : null;
         
         if (null == setter)
         {
            List parsedPath = parse(apSetter.getId());
            
            if (1 == parsedPath.size())
            {
               Method mthdSetter = Reflect.decodeMethod(instance.getClass(),
                     (String) parsedPath.get(0));
               if (null != mthdSetter)
               {
                  setter = new ResolvedMethod(mthdSetter);
                  
                  if ((null == methodCache) || methodCache.isEmpty())
                  {
                     methodCache = Collections.singletonMap(instance.getClass(), setter);
                  }
                  else
                  {
                     methodCache = CollectionUtils.copyMap(methodCache);
                     methodCache.put(instance.getClass(), setter);
                  }
                  
                  ((ModelElement) apSetter).setRuntimeAttribute(METHOD_CACHE, methodCache);
               }
            }
         }
      }
      
      if (trace.isDebugEnabled())
      {
         trace.debug("Access point is " + ((null != instance)
               ? "of type '" + instance.getClass().getName()
               : " null") + "'.");
      }

      if ((null != instance) && (null != setter))
      {
         try
         {
            if (1 == setter.argTypes.length)
            {
               evaluateSetter(setter.self, instance, setter.argTypes[0], value);
            }
            else
            {
               throw new PublicException("Setter for in path does not "
                     + "accept a single parameter: "
                     + Reflect.encodeMethod(setter.self));
            }
         }
         catch (InvocationTargetException ite)
         {
            trace.debug("Failed evaluating Java IN path.", ite.getTargetException());
            throw ite;
         }
         catch (PublicException pe)
         {
            throw pe;
         }
         catch (Exception x)
         {
            trace.debug("", x);
            throw new PublicException(x.getMessage());
         }
      }

      return instance;
   }
   
   public static Object evaluateSetter(Method mthdSetter, Object instance, Class argType,
         Object value) throws InvocationTargetException
   {
      if ((null != instance) && (null != mthdSetter))
      {
         try
         {
            if (trace.isDebugEnabled())
            {
               trace.debug("Setting attribute '" + value + "' for object '" + instance
                     + "' using method '" + mthdSetter.getName() + "'.");
            }

            mthdSetter.invoke(instance, new Object[] {Reflect.castValue(value, argType)});
         }
         catch (IllegalArgumentException iae)
         {
            trace.debug("", iae);
            throw new InvocationTargetException(iae, "Illegal argument for method "
                  + mthdSetter.getName() + " in class " + instance.getClass() + ".");
         }
         catch (IllegalAccessException iae)
         {
            trace.debug("", iae);
            throw new InvocationTargetException(iae, "The method with name "
                  + mthdSetter.getName() + " is not accessible in class "
                  + instance.getClass() + ".");
         }
         catch (InvalidValueException e)
         {
            final String msg = "Failed evaluating Java IN path due to an invalid value.";
            trace.debug(msg, e);
            throw new InvocationTargetException(e, msg);
         }
      }

      return instance;
   }

   public static Object evaluate(String inPath, Object accessPoint, Object value)
         throws InvocationTargetException
   {
      List parsedPath = parse(inPath);

      Object thisAfterEvaluation;
      if (parsedPath.isEmpty())
      {
         // replacing object as whole
         thisAfterEvaluation = value;
      }
      else
      {
         // object reference remains the same
         thisAfterEvaluation = accessPoint;

         if (trace.isDebugEnabled())
         {
            trace.debug("Access point is "
                  + ((null != accessPoint) ? "of type '"
                        + accessPoint.getClass().getName() : " null") + "'.");
         }

         Object currentThis = accessPoint;
         for (Iterator i = parsedPath.iterator(); (null != currentThis) && i.hasNext(); )
         {
            final String element = (String) i.next();

            Method method;
            try
            {
               try
               {
                  method = Reflect.decodeMethod(currentThis.getClass(), element);
               }
               catch (InternalException ie)
               {
                  throw new PublicException("The method with name '" + element
                        + "' in class '" + currentThis.getClass()
                        + "' does not exist or is not accessible.");
               }

               if (i.hasNext())
               {
                  currentThis = evaluateGetter(method, currentThis);

                  if ((null == currentThis) && trace.isDebugEnabled())
                  {
                     if (trace.isDebugEnabled())
                     {
                        trace.debug("Found null reference after method '" + element
                              + "', stopping evaluation.");
                     }
                  }
               }
               else
               {
                  if (trace.isDebugEnabled())
                  {
                     trace.debug("Setting attribute '" + value + "' for object '"
                           + currentThis + "' using method '" + method.getName()
                           + "'.");
                  }

                  Class[] methodArgTypes = method.getParameterTypes();
                  if (1 == methodArgTypes.length)
                  {
                     evaluateSetter(method, currentThis, methodArgTypes[0], value);
                  }
                  else
                  {
                     throw new PublicException("Final setter for in path does not "
                           + "accept a single parameter: "
                           + Reflect.encodeMethod(method));
                  }
               }
            }
            catch (InvocationTargetException x)
            {
               trace.debug("Failed evaluating Java IN path.", x.getTargetException());
               throw x;
            }
         }
      }

      return thisAfterEvaluation;
   }

   private JavaDataTypeUtils()
   {
      // no instances allowed
   }
}
