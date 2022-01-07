# Dockerfile

# base image
FROM node:alpine
RUN apk update && apk add socat

# create & set working directory
RUN mkdir -p /usr/src
WORKDIR /usr/src

# copy source files
COPY . /usr/src

# install dependencies
RUN npm install

# start app
RUN npm run build
EXPOSE 3000
CMD sh docker.sh