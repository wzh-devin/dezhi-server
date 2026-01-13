# 基础镜像
FROM docker.1ms.run/eclipse-temurin:17

# 设置元数据
LABEL maintainer="devin <wzh.devin@gmail.com>"
LABEL author="devin"
LABEL email="wzh.devin@gmail.com"
LABEL version="0.0.1"
LABEL description="Dezhi博客后台服务"

# 创建工作目录
WORKDIR /usr/src/app

# 打包文件
COPY ./dezhi-application/target/dezhi-application-1.0-SNAPSHOT.jar ./dezhi-1.0-SNAPSHOT.jar

# 暴露端口
EXPOSE 12010

# 修改容器时区
RUN rm -f /etc/localtime \
&& ln -sv /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
&& echo "Asia/Shanghai" > /etc/timezone

# 启动程序
CMD ["sh", "-c", "java ${JAVA_OPTS} -jar dezhi-1.0-SNAPSHOT.jar"]