package tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.junit.Test;

import binder.Binder;

public class BinderTests {
	
	@Test
	public void testGetFieldsValues() {
		TestObject testObject = new TestObject();
		try {
			Map<String, Object> map = Binder.getFieldsValues(testObject);
			assertTrue(map != null);
			assertTrue(map.containsKey("testField1"));
			assertTrue(map.get("testField1").equals("TestField1"));
			assertTrue(map.containsKey("testField2"));
			assertTrue((int)map.get("testField2") == 1);
			assertTrue(map.containsKey("testField3"));
			String[] testField3 = (String[])map.get("testField3");
			assertTrue(testField3[0].equals("one"));
			assertTrue(testField3[1].equals("two"));
			assertTrue(testField3[2].equals("three"));
			assertTrue(testField3[3].equals("four"));
			assertTrue(testField3[4].equals("five"));
		} catch (IllegalArgumentException | IllegalAccessException e) {
			fail("Exception: " + e.getMessage());
		}
	}

	@Test
	public void testBindTo() {
		TestObject testObject = new TestObject();
		TestObjectDTO testObjectDTO = null;
		try {
			testObjectDTO = Binder.bindToFields(TestObjectDTO.class, Binder.getFieldsValues(testObject));
			assertTrue(testObjectDTO != null);
			assertTrue(testObjectDTO.getClass().getField("testField1") != null );
			for (Field f : testObjectDTO.getClass().getFields()){
				if(f.getType().getName().equals(
						testObject.getClass().getField(f.getName()).getType().getName())){
					assertTrue(f.get(testObjectDTO).equals(
							testObject.getClass().getField(f.getName()).get(testObject)));
				}
			}
		} catch (InstantiationException | IllegalAccessException
				| NoSuchFieldException | SecurityException
				| IllegalArgumentException e) {
			fail("Exception: " + e.getMessage());
		}
	}

	@Test
	public void testBindToProperties() {
		TestObject testObject = new TestObject();
		TestObjectDTO testObjectDTO = null;
		try {
			testObjectDTO = Binder.bindToProperties(TestObjectDTO.class, Binder.getFieldsValues(testObject));
			assertTrue(testObjectDTO != null);
			assertTrue(testObjectDTO.getClass().getField("testField1") != null );
			for (Method m : testObjectDTO.getClass().getMethods()){
				if (!m.getName().matches("[Gg]et.*")) continue; 
				String dtoFieldName = m.getName().substring(3);
				for (Field f : testObject.getClass().getFields()){
					if (f.getName().equalsIgnoreCase(dtoFieldName) && m.getReturnType().getName().equalsIgnoreCase(
							f.getType().getName())){
						Object testObjectValue = f.get(testObject);
						Object testObjectDTOValue = m.invoke(testObjectDTO);
						assertTrue(testObjectValue.equals(testObjectDTOValue));
					
					}
				}
			}
		} catch (InstantiationException | IllegalAccessException
				| NoSuchFieldException | SecurityException
				| IllegalArgumentException | InvocationTargetException e) {
			fail("Exception: " + e.getMessage());
		}
	}

	@Test
	public void testBindToFieldsAndProperties() {
		TestObject testObject = new TestObject();
		TestObjectDTO testObjectDTO = null;
		try {
			testObjectDTO = Binder.bindToFieldsAndProperties(TestObjectDTO.class, Binder.getFieldsValues(testObject));
			assertTrue(testObjectDTO != null);
			assertTrue(testObjectDTO.getClass().getField("testField1") != null );
			for (Field f : testObjectDTO.getClass().getFields()){
				if(f.getType().getName().equals(
						testObject.getClass().getField(f.getName()).getType().getName())){
					assertTrue(f.get(testObjectDTO).equals(
							testObject.getClass().getField(f.getName()).get(testObject)));
				}
			}
			for (Method m : testObjectDTO.getClass().getMethods()){
				if (!m.getName().matches("[Gg]et.*")) continue; 
				String dtoFieldName = m.getName().substring(3);
				for (Field f : testObject.getClass().getFields()){
					if (f.getName().equalsIgnoreCase(dtoFieldName) && m.getReturnType().getName().equalsIgnoreCase(
							f.getType().getName())){
						Object testObjectValue = f.get(testObject);
						Object testObjectDTOValue = m.invoke(testObjectDTO);
						assertTrue(testObjectValue.equals(testObjectDTOValue));
					
					}
				}
			}
		} catch (InstantiationException | IllegalAccessException
				| NoSuchFieldException | SecurityException
				| IllegalArgumentException | InvocationTargetException e) {
			fail("Exception: " + e.getMessage());
		}
	}
}
