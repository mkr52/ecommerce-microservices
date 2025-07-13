# ecommerce-microservices

## For Loki evaluation, you need to set up a MinIO server to store the logs. 
Follow these steps before running the docker compose.
### Ensure the directory exists:
mkdir -p /Users/mohitkumar/Documents/Self-Learning/ecom-microservices/additional/evaluate-loki/.data/minio
### Change its ownership to your user:
sudo chown -R $(whoami) /Users/mohitkumar/Documents/Self-Learning/ecom-microservices/additional/evaluate-loki/.data/minio
Retry your docker-compose command.
