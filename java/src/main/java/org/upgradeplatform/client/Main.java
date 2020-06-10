package org.upgradeplatform.client;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.eclipse.jdt.annotation.NonNull;
import org.upgradeplatform.interfaces.ResponseCallback;
import org.upgradeplatform.requestbeans.MetricUnit;
import org.upgradeplatform.responsebeans.ErrorResponse;
import org.upgradeplatform.responsebeans.FeatureFlag;
import org.upgradeplatform.responsebeans.InitRequest;
import org.upgradeplatform.responsebeans.Metric;
import org.upgradeplatform.utils.Utils.MetricMetaData;



public class Main {
	public static void main(String []args) throws InterruptedException, ExecutionException
	{
		String baseUrl = "http://development-upgrade-experiment-app.eba-gp6psjut.us-east-1.elasticbeanstalk.com";
		String userId = "user1";
	
		
		String[] classList = {"1","2","3","4","5"};
		String[] teacherList = {"1","2","7"};
		String[] aliases = {"user1alias1","user1alias2","user1alias3","user1alias4","user1alias5"};
		
		
		HashMap<String, List<String>> group = new HashMap<>();
		group.put("classes", Arrays.asList(classList));
		group.put("teachers", Arrays.asList(teacherList));
		
		String [] allowedData= {"InProgress", "Complete"};
		String [] key= {"hellow", "completion2"};
		MetricUnit[] children = {};
		
		MetricUnit metricUnit = new MetricUnit( key,children, MetricMetaData.continuous.toString(), allowedData);
	
		 MetricUnit[] metrics = {metricUnit};
		

		try(ExperimentClient experimentClient = new ExperimentClient(userId, "BearerToken", baseUrl)){
		    CompletableFuture<String> result = new CompletableFuture<>();

            System.out.println(prefix() + "initiating requests");
            
		    experimentClient.addMetrics( metrics, new ResponseCallback<List<Metric>>(){
		        @Override
		        public void onSuccess(List<Metric> data){
		        	result.complete(prefix() + "retrieved " + data.size() + 
                    		" FeatureFlag responses; theme response: " + 
                    		data.get(0).getKey().toString() + "  type:  "+ data.get(0).getType());
		        }

		        @Override
		        public void onError(@NonNull ErrorResponse error){
                    result.completeExceptionally(new Exception(prefix() + error.toString()));
		        }
		    });

            System.out.println(prefix() + result.getNow("not complete yet"));
            String rs = result.get();
            System.out.println(prefix() + rs);
		}
	}

	private static final String prefix() {
	    return "on thread " + Thread.currentThread().getName() + " at " + System.currentTimeMillis() + ": ";
	}
}
