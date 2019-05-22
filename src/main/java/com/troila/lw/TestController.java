package com.troila.lw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.ZoneAwareLoadBalancer;

@RestController
@Configuration
public class TestController {

	//RestTemplate本身是Spring下的一個很普通的rest調用的客戶端而已。
	//但為什麼餵它加上了@LoadBalanced註解後就可以實現負載均衡的調用服務端呢？
	//這是因為這個RestTemplate本身有一個攔截器的機制。所以它才可以和這個LoadBalanced註解協同產生出負載均衡的效果
	/** 所以這裡的槽點在於，攔截器和註解的組合使用，後續可以考慮如何用自己的方法來實現這種組合工作的設計模式*/
	@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	
	@GetMapping("/router")
	@ResponseBody
	public String router() {
		RestTemplate rtl = getRestTemplate();
		//下面的first-police的出處，在first-police工程的application.yml配置文件的“spring:application:”這個屬性中
		//因為服務的提供者是警察工程，而非eureka的服務端（亦即並非first-114工程）。因此這裡要寫服務提供者的服務名
		//當然也可以直接寫成
		String json = rtl.getForObject("http://ribbon-lb-provider/call/10", String.class);
		return json;
	}
	
	//下面的例子，使用的是spring對ribbon封裝後得到的LoadBalancerClient來實現負載均衡的功能。
	@Autowired
	private LoadBalancerClient client;
	
	@RequestMapping(value = "/lb", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceInstance lb() {
		//ServiceInstance是獲取服務實例的。所謂的服務實例，是指一個服務的多個實例中的某一個
		//再由LoadBalancerClient來幫我們選擇一個服務實例進行調用，這就是LoadBalancerClient的作用
		ServiceInstance si = client.choose("ribbon-lb-provider");
		return si;
	}
	
	//看一下springcloud中集成了哪些ribbon的配置。下面這個類是幫我們創建客戶端的。
	@Autowired
	private SpringClientFactory factory;
	
	@RequestMapping(value = "/lb", method = RequestMethod.GET)
	public String factory() {
		ILoadBalancer lb = factory.getLoadBalancer("default");
		//打印默認的負載均衡器
		System.out.println(lb.getClass().getName());
		
		//通過打印上面的lb的名稱，知道spring默認使用的是ZoneAwareLoadBalancer這個負載均衡器
		ZoneAwareLoadBalancer lb1 = (ZoneAwareLoadBalancer)factory.getLoadBalancer("default");
		//若不加上.getClass().getName()，顯示的效果就是這樣“com.netflix.loadbalancer.ZoneAvoidanceRule@6570b709”
		System.out.println(lb1.getRule().getClass().getName());
		
		//再看一下使用我們自定義的規則是如何的
		ZoneAwareLoadBalancer lb2 = (ZoneAwareLoadBalancer)factory.getLoadBalancer("ribbon-lb-provider");
		//若不加上.getClass().getName()，顯示的效果就是這樣“com.netflix.loadbalancer.ZoneAvoidanceRule@6570b709”
		System.out.println(lb2.getRule().getClass().getName());
		
		return null;
	}
	
	
}
