當前工程既是Eureka高層服務提供者，也是底層服務的調用者，
通過詢問eureka集群（如first-114），獲取Eureka服務列表
進而從服務列表中調用某一個自己關心的服務提供者（如first-police服務）提供的服務。

從系統架構上來講。first-114相當於底層的服務容器tomcat
而first-police和first-person都是部署於tomcat中的兩套程序。
區別在於，first-police是最底層的服務提供者，而first-person是高級的服務提供者
這個first-person對外服務的提供，依賴於底層first-police
而first-client相當於用戶終端（手機端、web端等），它發起請求到first-person，獲取由first-person返回的數據