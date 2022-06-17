FROM moxm/java:1.8-full as builder

MAINTAINER wangiegie@gmail.com

ENV TZ=Asia/Shanghai
ENV JAVA_OPTS="-Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom"

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /yqm-shop-admin

WORKDIR /yqm-shop-admin

EXPOSE 8001

ADD ./target/yqm-shop-admin-3.2.jar ./

CMD java $JAVA_OPTS -jar yqm-shop-admin-3.2.jar --spring.profiles.active=docker
