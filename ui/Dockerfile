FROM node:14 as BUILD

WORKDIR /app
COPY package*.json /app/
RUN npm install

COPY . /app
RUN npm run build

FROM nginx

COPY --from=BUILD /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80