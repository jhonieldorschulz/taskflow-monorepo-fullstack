FROM node:18
WORKDIR /app
COPY . .
RUN npm install && npm run build
EXPOSE 4200
CMD ["npm", "start"]