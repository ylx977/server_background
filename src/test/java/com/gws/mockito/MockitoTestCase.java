
package com.gws.mockito;

import java.util.LinkedList;
import java.util.List;

import com.gws.base.GwsBaseTestCase;
import com.gws.utils.GwsLogger;

import static org.mockito.Mockito.*;
/**
 * Mockito test demo
 *
 * @version 
 * @author wangdong  2016年6月25日 下午2:09:33
 * 
 */
public class MockitoTestCase extends GwsBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		GwsLogger.info("setUp");
	}

	public void testVerify(){
		//mock creation
		 List mockedList = mock(List.class);
		 
		 //using mock object
		 mockedList.add("one");
		 mockedList.clear();
		 
		 //verification
		 verify(mockedList).add("one");
		 verify(mockedList).clear();
		 
	}
	
	public void testStubbing(){
		 //You can mock concrete classes, not just interfaces
		 LinkedList mockedList = mock(LinkedList.class);

		 //stubbing
		 when(mockedList.get(0)).thenReturn("first");
		 when(mockedList.get(1)).thenThrow(new RuntimeException());

		 //following prints "first"
		 GwsLogger.info(mockedList.get(0).toString());

		 //following throws runtime exception

		 //following prints "null" because get(999) was not stubbed
		// GwsLogger.info(mockedList.get(999).toString());

		 //Although it is possible to verify a stubbed invocation, usually it's just redundant
		 //If your code cares what get(0) returns, then something else breaks (often even before verify() gets executed).
		 //If your code doesn't care what get(0) returns, then it should not be stubbed. Not convinced? See here.
		 verify(mockedList).get(0);
		 
	}
}
