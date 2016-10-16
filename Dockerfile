FROM flurdy/activator

MAINTAINER Joao Vazao Vasques
ENV WORK_DIR /app
RUN mkdir $WORK_DIR
ADD . $WORK_DIR

