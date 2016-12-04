package krugdev.me;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import krugdev.me.TestEntity;



public class DBServiceTest {
	
	private static DBService dbService = new DBService("sax_her_test");
	private final String QUERY_NAME = "TestEntity.findEntityByName";
	private Map<String, Object> queryParam = new HashMap<>();
	
	@Before
	public void cleanDBandSetQueryParam() {
		List<String> tablesNames = new ArrayList<>(); 
		tablesNames.add("TestEntity");
		JDBCExample.deleteTable(tablesNames);
		queryParam.put("name", "test");
	}
	
	@AfterClass
	public static void closeDBService() {
		dbService.close();
	}
	
	@Test
	public void emptyTest(){
		
	}
	
	@Test
	public void shouldAddEntityToDB() {
		TestEntity entity = new TestEntity("test", 1);
		dbService.persist(entity);		
		Optional<TestEntity> returnEntity = dbService.findEntity(QUERY_NAME, queryParam, TestEntity.class);
		assertEquals(entity.getId(), returnEntity.get().getId());
	}
	
	@Test
	public void shouldUpdateEntityInDB() {
		TestEntity entity = new TestEntity("oldName",1);
		dbService.persist(entity);
		entity.setName("test");
		dbService.update(entity);
		Optional<TestEntity> returnEntity = dbService.findEntity(QUERY_NAME, queryParam, TestEntity.class);
		assertEquals(entity.getId(), returnEntity.get().getId());
	}
	/*
	@Test
	public void shouldReturnListOfEntitiesFormQuery() {
		TestEntity entity = new TestEntity("test");
		TestEntity entity1 = new TestEntity("test");
		TestEntity entity2 = new TestEntity("test");
		dbService.persist(entity, entity1, entity2);
		List<TestEntity> entities = dbService.findEntities(QUERY_NAME, queryParam, TestEntity.class);
		assertEquals(3, entities.size());
	}
	
	@Test
	public void shouldRemoveEntity() {
		TestEntity entity = new TestEntity("test");
		dbService.persist(entity);
		dbService.remove(entity);
		Optional<TestEntity> optionalTest= dbService.findEntity(QUERY_NAME, queryParam, TestEntity.class);
		assertFalse(optionalTest.isPresent());
	} //*/
}
