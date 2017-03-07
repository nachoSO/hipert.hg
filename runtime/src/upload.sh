#!/bin/bash

HOSTNAME=89.97.65.173
USERNAME=unimore
PORT=10022

FOLDER=/home/unimore/__remote/hg
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

DEST=${USERNAME}@${HOSTNAME}:${FOLDER}

# echo "Copy to ${DEST} .."
echo -e "${YELLOW} Uploading ...${NC}"
scp -P${PORT} *.c ${DEST}
scp -P${PORT} *.h ${DEST}
scp -P${PORT} Makefile ${DEST}
scp -P${PORT} config/bostan-gomp/* ${DEST}/config/bostan-gomp/
# ${RSAFLAG}