package com.troila.lw;

import org.springframework.cloud.netflix.ribbon.RibbonClient;

//@RibbonClient註解的主要作用就是向spring容器中註冊一套負載均衡的規則，規則名稱叫做“ribbon-lb-provider”
//也就是說，當有外部請求通過當前invoker工程訪問http://ribbon-lb-provider/下的所有服務時，都會被這套負載均衡規則控制
//當前，這裡可以不使用註解，而降註解相關的東西放到配置文件中。詳見當前工程的配置文件。

//其中，在name屬性就是限制被調用的服務的名稱，注意是服務的名稱，configuration是負載均衡的規則bean
//@RibbonClient(name = "ribbon-lb-provider", configuration = MyConfig.class)
public class MyClient {

}
