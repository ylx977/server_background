
package com.gws.disruptor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;

/**
 * 并发框架测试
 *
 * @version 
 * @author wangdong  2016年6月22日 下午6:13:22
 * 
 */
public class TestDisruptor {
	
	 public static class DataEvent
	    {
	        Object input;
	        Object[] output;

	        public DataEvent(int size)
	        {
	            output = new Object[size];
	        }

	        public static final EventFactory<DataEvent> FACTORY = new EventFactory<DataEvent>()
	        {
	            @Override
	            public DataEvent newInstance()
	            {
	                return new DataEvent(3);
	            }
	        };
	    }

	 
	    public static class TransformingHandler implements EventHandler<DataEvent>
	    {
	        private final int outputIndex;

	        public TransformingHandler(int outputIndex)
	        {
	            this.outputIndex = outputIndex;
	        }

	        @Override
	        public void onEvent(DataEvent event, long sequence, boolean endOfBatch) throws Exception
	        {
	            // Do Stuff.
	            event.output[outputIndex] = doSomething(event.input);
	        }

	        private Object doSomething(Object input)
	        {
	            // Do required transformation here....
	            return input;
	        }
	    }

	    public static class CollatingHandler implements EventHandler<DataEvent>
	    {
	        @Override
	        public void onEvent(DataEvent event, long sequence, boolean endOfBatch) throws Exception
	        {
	            collate(event.output);
	        }

	        private void collate(Object[] output)
	        {
	            // Do required collation here....
	        	System.out.println(output);
	        }
	    }
	    
	/**
	 * 【请在此输入描述文字】
	 * 
	 * @author wangdong 2016年6月22日
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 这是系统自动生成描述，请在此补完后续代码
		 Executor executor = Executors.newFixedThreadPool(4);
	        Disruptor<DataEvent> disruptor = new Disruptor<DataEvent>(
	                DataEvent.FACTORY, 1024, DaemonThreadFactory.INSTANCE);
	        
	        TransformingHandler handler1 = new TransformingHandler(0);
	        TransformingHandler handler2 = new TransformingHandler(1);
	        TransformingHandler handler3 = new TransformingHandler(2);
	        CollatingHandler collator = new CollatingHandler();

	        disruptor.handleEventsWith(handler1, handler2, handler3).then(collator);

	        disruptor.start();
	}

}
