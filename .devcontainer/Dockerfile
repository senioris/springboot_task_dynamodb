ARG JAVA_VERSION="17"
FROM mcr.microsoft.com/openjdk/jdk:21-ubuntu

RUN apt update -y &&\
    apt install vim -y
RUN ln -s /usr/lib/jvm/msopenjdk-21 /usr/lib/jvm/msopenjdk-current

ARG HOME_DIR='/root'
RUN mkdir -p $HOME_DIR/.aws
COPY credential/.aws/* $HOME_DIR/.aws/
