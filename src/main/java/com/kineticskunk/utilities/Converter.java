package com.kineticskunk.utilities;

import com.google.common.collect.Lists; 

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.lang3.BooleanUtils; 
import org.apache.commons.lang3.StringUtils; 

import java.lang.reflect.Constructor; 
import java.lang.reflect.InvocationTargetException; 
import java.math.BigDecimal; 
import java.math.BigInteger; 
import java.net.InetAddress; 
import java.net.MalformedURLException; 
import java.net.URL; 
import java.net.UnknownHostException; 
import java.util.List; 
import java.util.Locale;

/**
 * @author yodaqua
 * Added by Donovan Mulder on the 25 April 2016
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.codec.binary.Base64;


/**
 * Created with IntelliJ IDEA ( LivePerson : www.liveperson.com ) 
 * 
 * Package: com.framework.utils.conversion 
 * 
 * Name   : Converter  
 * 
 * User   : solmarkn / Dani Vainstein 
 * 
 * Date   : 2015-03-05  
 * 
 * Time   : 21:21 
 * 
 */ 

public class Converter { 

	/** Constant for the prefix of binary numbers.*/ 
	private static final String BIN_PREFIX = "0b"; 

	/** Constant for the prefix of hex numbers.*/ 
	private static final String HEX_PREFIX = "0x"; 

	/** Constant for the radix of hex numbers.*/ 
	private static final int HEX_RADIX = 16; 

	/** Constant for the radix of binary numbers.*/ 
	private static final int BIN_RADIX = 2; 

	/** Constant for the argument classes of the Number constructor that takes a String. */ 
	private static final Class<?>[] CONSTR_ARGS = {String.class};
	
	static final Base64 base64 = new Base64();

    public static String serializeObjectToString(Object object) throws IOException {
        try (
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                GZIPOutputStream gzipOutputStream = new GZIPOutputStream(arrayOutputStream);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(gzipOutputStream);) {
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            return new String(base64.encode(arrayOutputStream.toByteArray()));
        }
    }

    public static Object deserializeObjectFromString(String objectString) throws IOException, ClassNotFoundException {
        try (
                ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(base64.decode(objectString));
                GZIPInputStream gzipInputStream = new GZIPInputStream(arrayInputStream);
                ObjectInputStream objectInputStream = new ObjectInputStream(gzipInputStream)) {
            return objectInputStream.readObject();
        }
    }
	
	
	/**
	 * Added by Donovan Mulder
	 * Date: 25 April 2016
	 * @author yodaqua
	 * 
	 * @param str
	 * @return
	 */
	public static String getNumeric(String str) {
		String s = "";
		for (char c : str.toCharArray()) {
			if (Character.isDigit(c)) {
				s = s + c;
			}
		}
		return s;
	}
	
	/**
	 * Added by Donovan Mulder
	 * Date: 25 April 2016
	 * @author yodaqua
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		for (char c : str.toCharArray()) {
			if (!Character.isDigit(c)) return false;
		}
		return true;
	}
	
	/**
	 * Added by Donovan Mulder
	 * Date: 25 April 2016
	 * @author yodaqua
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isBoolean(String value) {
		if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
			return true;
		}
		return false;
	}
	
	/**
	 * Convert the specified object into a Locale. 
	 * 
	 * @param value the value to convert 
	 * @return the converted value 
	 * @throws ConversionException thrown if the value cannot be converted to a Locale 
	 */ 
	public static Locale toLocale( Object value ) throws ConversionException 
	{ 
		if ( value instanceof Locale ) 
		{ 
			return ( Locale ) value; 
		} 
		else if ( value instanceof String ) 
		{ 
			List<String> elements = Lists.newArrayList( StringUtils.split( ( String ) value, '_' ) ); 
			int size = elements.size(); 

			if ( size >= 1 && ( ( elements.get( 0 ) ).length() == 2 || ( elements.get( 0 ) ).length() == 0 ) ) 
			{ 
				String language = elements.get( 0 ); 
				String country = ( size >= 2 ) ? elements.get( 1 ) : ""; 
				String variant = ( size >= 3 ) ? elements.get( 2 ) : ""; 

				return new Locale( language, country, variant ); 
			} 
			else 
			{ 
				throw new ConversionException( "The value " + value + " can't be converted to a Locale" ); 
			} 
		} 
		else 
		{ 
			throw new ConversionException( "The value " + value + " can't be converted to a Locale" ); 
		} 
	} 

	/**
	 * Convert the specified object into an URL. 
	 * 
	 * @param value the value to convert 
	 * @return the converted value 
	 * @throws ConversionException thrown if the value cannot be converted to an URL 
	 */ 
	public static URL toURL( Object value ) throws ConversionException 
	{ 
		if ( value instanceof URL ) 
		{ 
			return ( URL ) value; 
		} 
		else if ( value instanceof String ) 
		{ 
			try 
			{ 
				return new URL( ( String ) value ); 
			} 
			catch ( MalformedURLException e ) 
			{ 
				throw new ConversionException( "The value " + value + " can't be converted to an URL", e ); 
			} 
		} 
		else 
		{ 
			throw new ConversionException( "The value " + value + " can't be converted to an URL" ); 
		} 
	} 

	/**
	 * Convert the specified value into an internet address. 
	 * 
	 * @param value the value to convert 
	 * @return the converted value 
	 * @throws ConversionException thrown if the value cannot be converted to a InetAddress 
	 * 
	 * @since 1.5 
	 */ 
	public static InetAddress toInetAddress( Object value ) throws ConversionException 
	{ 
		if ( value instanceof InetAddress ) 
		{ 
			return ( InetAddress ) value; 
		} 
		else if ( value instanceof String ) 
		{ 
			try 
			{ 
				return InetAddress.getByName( ( String ) value ); 
			} 
			catch ( UnknownHostException e ) 
			{ 
				throw new ConversionException( "The value " + value + " can't be converted to a InetAddress", e ); 
			} 
		} 
		else 
		{ 
			throw new ConversionException( "The value " + value + " can't be converted to a InetAddress" ); 
		} 
	} 

	/**
	 * Convert the specified object into an Integer. 
	 * 
	 * @param value the value to convert 
	 * @return the converted value 
	 * @throws ConversionException thrown if the value cannot be converted to an integer 
	 */ 
	public static Integer toInteger( Object value ) throws ConversionException 
	{ 
		Number n = toNumber( value, Integer.class ); 
		if ( n instanceof Integer ) 
		{ 
			return ( Integer ) n; 
		} 
		else 
		{ 
			return new Integer( n.intValue() ); 
		} 
	} 

	/**
	 * Convert the specified object into a Long. 
	 * 
	 * @param value the value to convert 
	 * @return the converted value 
	 * @throws ConversionException thrown if the value cannot be converted to a Long 
	 */ 
	public static Long toLong( Object value ) throws ConversionException 
	{ 
		Number n = toNumber( value, Long.class ); 
		if ( n instanceof Long ) 
		{ 
			return ( Long ) n; 
		} 
		else 
		{ 
			return new Long( n.longValue() ); 
		} 
	} 

	/**
	 * Convert the specified object into a Float. 
	 * 
	 * @param value the value to convert 
	 * @return the converted value 
	 * @throws ConversionException thrown if the value cannot be converted to a Float 
	 */ 
	public static Float toFloat( Object value ) throws ConversionException 
	{ 
		Number n = toNumber( value, Float.class ); 
		if ( n instanceof Float ) 
		{ 
			return ( Float ) n; 
		} 
		else 
		{ 
			return new Float( n.floatValue() ); 
		} 
	} 

	/**
	 * Convert the specified object into a Double. 
	 * 
	 * @param value the value to convert 
	 * @return the converted value 
	 * @throws ConversionException thrown if the value cannot be converted to a Double 
	 */ 
	public static Double toDouble( Object value ) throws ConversionException 
	{ 
		Number n = toNumber( value, Double.class ); 
		if ( n instanceof Double ) 
		{ 
			return ( Double ) n; 
		} 
		else 
		{ 
			return new Double( n.doubleValue() ); 
		} 
	} 

	/**
	 * Convert the specified object into a BigInteger. 
	 * 
	 * @param value the value to convert 
	 * @return the converted value 
	 * @throws ConversionException thrown if the value cannot be converted to a BigInteger 
	 */ 
	public static BigInteger toBigInteger( Object value ) throws ConversionException 
	{ 
		Number n = toNumber( value, BigInteger.class ); 
		if ( n instanceof BigInteger ) 
		{ 
			return ( BigInteger ) n; 
		} 
		else 
		{ 
			return BigInteger.valueOf( n.longValue() ); 
		} 
	} 

	/**
	 * Convert the specified object into a BigDecimal. 
	 * 
	 * @param value the value to convert 
	 * @return the converted value 
	 * @throws ConversionException thrown if the value cannot be converted to a BigDecimal 
	 */ 
	public static BigDecimal toBigDecimal( Object value ) throws ConversionException 
	{ 
		Number n = toNumber( value, BigDecimal.class ); 
		if ( n instanceof BigDecimal ) 
		{ 
			return ( BigDecimal ) n; 
		} 
		else 
		{ 
			return new BigDecimal( n.doubleValue() ); 
		} 
	} 

	/**
	 * Tries to convert the specified object into a number object. This method 
	 * is used by the conversion methods for number types. Note that the return 
	 * value is not in always of the specified target class, but only if a new 
	 * object has to be created. 
	 * 
	 * @param value the value to be converted (must not be <b>null</b>) 
	 * @param targetClass the target class of the conversion (must be derived 
	 * from {@code java.lang.Number}) 
	 * @return the converted number 
	 * @throws ConversionException if the object cannot be converted 
	 */ 
	static Number toNumber( Object value, Class<?> targetClass ) throws ConversionException 
	{ 
		if ( value instanceof Number ) 
		{ 
			return ( Number ) value; 
		} 
		else 
		{ 
			String str = value.toString(); 
			if ( str.startsWith( HEX_PREFIX ) ) 
			{ 
				try 
				{ 
					return new BigInteger( str.substring( HEX_PREFIX.length() ), HEX_RADIX ); 
				} 
				catch ( NumberFormatException nex ) 
				{ 
					throw new ConversionException( "Could not convert " + str 
							+ " to " + targetClass.getName() 
							+ "! Invalid hex number.", nex ); 
				} 
			} 

			if ( str.startsWith( BIN_PREFIX ) ) 
			{ 
				try 
				{ 
					return new BigInteger( str.substring( BIN_PREFIX.length() ), BIN_RADIX ); 
				} 
				catch ( NumberFormatException nex ) 
				{ 
					throw new ConversionException( "Could not convert " + str 
							+ " to " + targetClass.getName() 
							+ "! Invalid binary number.", nex ); 
				} 
			} 

			try 
			{ 
				Constructor<?> constr = targetClass.getConstructor( CONSTR_ARGS ); 
				return ( Number ) constr.newInstance( new Object[] { str } ); 
			} 
			catch ( InvocationTargetException itex ) 
			{ 
				throw new ConversionException( "Could not convert " + str 
						+ " to " + targetClass.getName(), itex 
						.getTargetException() ); 
			} 
			catch ( Exception ex ) 
			{ 
				// Treat all possible exceptions the same way 
				throw new ConversionException( 
						"Conversion error when trying to convert " + str 
						+ " to " + targetClass.getName(), ex 
						); 
			} 
		} 
	} 

	/**
	 * Convert the specified object into a Boolean. Internally the 
	 * {@code org.apache.commons.lang.BooleanUtils} class from the 
	 * <a href="http://commons.apache.org/lang/">Commons Lang</a> 
	 * project is used to perform this conversion. This class accepts some more 
	 * tokens for the boolean value of <b>true</b>, e.g. {@code yes} and 
	 * {@code on}. Please refer to the documentation of this class for more 
	 * details. 
	 * 
	 * @param value the value to convert 
	 * @return the converted value 
	 * @throws ConversionException thrown if the value cannot be converted to a boolean 
	 */ 
	public static Boolean toBoolean(Object value) throws ConversionException 
	{ 
		if (value instanceof Boolean) 
		{ 
			return (Boolean) value; 
		} 
		else if (value instanceof String) 
		{ 
			Boolean b = BooleanUtils.toBooleanObject( ( String ) value ); 
			if (b == null) 
			{ 
				throw new ConversionException("The value " + value + " can't be converted to a Boolean object"); 
			} 
			return b; 
		} 
		else 
		{ 
			throw new ConversionException("The value " + value + " can't be converted to a Boolean object"); 
		} 
	} 

}