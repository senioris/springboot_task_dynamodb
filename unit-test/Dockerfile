FROM public.ecr.aws/amazoncorretto/amazoncorretto:21

RUN yum install python3 -y \
    && pip3 install awscli \
    && pip3 install awscli-local

ARG HOME_DIR=/root
RUN mkdir -p $HOME_DIR/.aws
COPY .aws/* $HOME_DIR/.aws/