socat TCP-LISTEN:8080,fork TCP:backend:8080 &
npm run start