ssh -tt -p ${SERVER_PORT} ${SERVER_USER}@${SERVER_HOST} -i key.txt -o StrictHostKeyChecking=no << 'ENDSSH'

cd capstone-2024-22
git pull origin "deploy/v1"
docker-compose up --build -d
ENDSSH