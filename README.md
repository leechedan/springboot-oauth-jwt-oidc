# Spring Boot
Examples using the Spring Boot Framework

. 启动进程jwt-generator  authorization-server  resource-server

. 获取token  curl http://localhost:8081/api/token

. 使用token发送请求 http://localhost:8080/api/orders/680a4f3e-2247-4815-b6df-b4a33f342a69，即得到404，进入OrderResource.get(uuid)

重点：token的生成必须由签发方，resource-server接到token后需要向authorization-server进行接口认证拿到kid（进行缓存），验签