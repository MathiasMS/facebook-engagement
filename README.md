# facebook-engagement
Obtain the shares and likes of the Facebook page that you want and discover the engagement of all post by hour.
## Installation
Put on "src\main\java\com\server\dao" the accessToken of your Facebook Api.
```java
//AccessToken for make the request to Facebook.
String accessToken = "";
					
con.setRequestProperty("Authorization", accessToken);
```
# Getting started

### Docker
```
sudo docker build -t [name]
```
Then:
```
sudo docker-compose up
```
