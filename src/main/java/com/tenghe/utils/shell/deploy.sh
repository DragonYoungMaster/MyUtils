#!/bin/bash

CURRENT_DIR=`pwd`

cas_client_dir=permission-cas-client
data_dir=permission-data
permission_client_dir=permission-client
DEPLOY_CAS_CLIENT_RESULT=failed
DEPLOY_DATA_RESULT=failed
DEPLOY_PERMISSION_CLIENT_RESULT=failed
SUCCEED=succeed
FAILED=failed

function setModuleDir() {
  for dir in $(ls)
  do
    if [[ -d $dir && $dir =~ $cas_client_dir ]]; then
      cas_client_dir=$dir
    elif [[ -d $dir && $dir =~ $data_dir ]]; then
       data_dir=$dir
    elif [[ -d $dir && $dir =~ $permission_client_dir ]]; then
       permission_client_dir=$dir
    fi
  done
}

function deploy_cas_client() {
  cd $CURRENT_DIR/$cas_client_dir
  mvn deploy -DskipTests
  if [ $? -eq 0 ]; then
    echo deploy cas-client succeed
    DEPLOY_CAS_CLIENT_RESULT=succeed
  else
    echo deploy cas-client failed
    DEPLOY_CAS_CLIENT_RESULT=failed
  fi
  cd $CURRENT_DIR
}

function deploy_data_client() {
  cd $CURRENT_DIR/$data_dir
  mvn deploy -DskipTests
  if [ $? -eq 0 ]; then
    echo deploy dependencies succeed
    DEPLOY_DATA_RESULT=succeed
  else
    echo deploy dependencies failed
    DEPLOY_DATA_RESULT=failed
  fi
  cd $CURRENT_DIR
}

function deploy_permission_client() {
  cd $CURRENT_DIR/$permission_client_dir
  mvn deploy -DskipTests
  if [ $? -eq 0 ]; then
    echo deploy permission-client succeed
    DEPLOY_PERMISSION_CLIENT_RESULT=succeed
  else
    echo deploy permission-client failed
    DEPLOY_PERMISSION_CLIENT_RESULT=failed
  fi
  cd $CURRENT_DIR
}

function deploy_all() {
  setModuleDir
  deploy_cas_client
  if [ "$SUCCEED" == "$DEPLOY_CAS_CLIENT_RESULT" ]; then
    deploy_data_client
    if [ "$SUCCEED" == "$DEPLOY_DATA_RESULT" ]; then
      deploy_permission_client
    fi
  fi
}

deploy_all
