run:
	docker compose -f docker/docker-compose.yaml up --build
stop:
	docker compose -f docker/docker-compose.yaml down
