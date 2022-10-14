#!/bin/bash

docker run --rm \
	--network=unicom-backend_default \
	-v `pwd`:`pwd` \
	-e POSTGRES_HOST=postgres \
	-e POSTGRES_PORT=5432 \
	unicom-backend /app/unicom-backend.jar -i `pwd`/preprocess-data/belgium_subset/out.json
