#!/bin/bash

# ultra_permission前端和后端编译打包的目标地址， 按自己的实际地址修改
ultra_permission_frontend_dist=/d/ultrapower/git/ultra-permission-frontend/dist/

CURRENT_DIR=`pwd`
MAVEN_CMD=mvn
MVN_MODULE_ARGS=""
BUILD_DEPENDENCY_RESULT=x
BUILD_CAS_CLIENT_RESULT=x
BUILD_PERMISSION_CLIENT_RESULT=x
BUILD_CAS_SERVER_RESULT=x
BUILD_PERMISSION_SERVER_RESULT=x

cas_server_dir=permission-cas-server
permission_server_dir=permission-server
cas_client_dir=permission-cas-client
data_dir=permission-data
permission_client_dir=permission-client
SUCCEED=succeed
FAILED=failed
ultra_permission_server_target=

function help() {
    echo "Usage: build.sh [all|ps|cas|data|client]"
    echo "Option："
    echo "    -h|help: show this usage"
    echo "    all: 全部项目, 缺省选项"
    echo "    ps:  build permission-server, all dependencies and all client"
    echo "    cas: build cas-server and all dependencies"
    echo "    data: build permission-data"
    echo "    client: build cas-client and permission-client"
}

function setModuleDir() {
  for dir in $(ls)
  do
    if [[ -d $dir && $dir =~ $cas_server_dir ]]; then
      cas_server_dir=$dir
    elif [[ -d $dir && $dir =~ $permission_server_dir ]]; then
       permission_server_dir=$dir
    elif [[ -d $dir && $dir =~ $cas_client_dir ]]; then
       cas_client_dir=$dir
    elif [[ -d $dir && $dir =~ $data_dir ]]; then
       data_dir=$dir
    elif [[ -d $dir && $dir =~ $permission_client_dir ]]; then
       permission_client_dir=$dir
    fi
  done
}

function setPermissionServerBuildTarget() {
  targetPath=$CURRENT_DIR/$permission_server_dir/ultra-permission-server/target
  targetName=ultra-permission-server-
  for ps_target in $(ls $targetPath); do
    if [[ -d $targetPath/$ps_target && $ps_target =~ $targetName ]]; then
      ultra_permission_server_target=$targetPath/$ps_target
    fi
  done
}

function build_dependency() {
  cd $data_dir
  $MAVEN_CMD clean install -DskipTests
  if [ $? -eq 0 ]; then
    echo build dependency permission-data succeed
    BUILD_DEPENDENCY_RESULT=succeed
  else
    echo build dependency permission-data failed
    BUILD_DEPENDENCY_RESULT=failed
  fi
  cd $CURRENT_DIR

}

function build_client() {
  cd $CURRENT_DIR/$cas_client_dir
  $MAVEN_CMD clean install -DskipTests
  if [ $? -eq 0 ]; then
    echo build cas-client succeed
    BUILD_CAS_CLIENT_RESULT=succeed
  else
    echo build cas-client failed
    BUILD_CAS_CLIENT_RESULT=failed
    cd $CURRENT_DIR
    exit 1
  fi

  cd $CURRENT_DIR/$permission_client_dir
  $MAVEN_CMD clean install -DskipTests
  if [ $? -eq 0 ]; then
    echo build permission-client succeed
    BUILD_PERMISSION_CLIENT_RESULT=succeed
  else
    echo build permission-client failed
    BUILD_PERMISSION_CLIENT_RESULT=failed
  fi
  cd $CURRENT_DIR
}

function build_permission() {
  build_dependency
  if [ "$SUCCEED" == "$BUILD_DEPENDENCY_RESULT" ]; then
    build_client
    if [ "$SUCCEED" == "$BUILD_PERMISSION_CLIENT_RESULT" ]; then
       cd $permission_server_dir
       $MAVEN_CMD clean package -DskipTests
       if [ $? -eq 0 ]; then
         echo build permission-server succeed
         BUILD_PERMISSION_SERVER_RESULT=succeed
         copy_permission_frontend
       else
         echo build permission-server failed
         BUILD_PERMISSION_SERVER_RESULT=failed
      fi
      cd $CURRENT_DIR
    fi
  fi
}

function copy_permission_frontend() {
  setPermissionServerBuildTarget
  if [[ -d $ultra_permission_server_target ]]; then
    echo Copying ultra-permission-frontend dist to target: $ultra_permission_server_target
    cp -rf $ultra_permission_frontend_dist/* $ultra_permission_server_target
  fi
}

function build_cas_server() {
  build_dependency
  if [ "$SUCCEED" == "$BUILD_DEPENDENCY_RESULT" ]; then
    cd $cas_server_dir
    $MAVEN_CMD clean package -DskipTests
    if [ $? -eq 0 ]; then
      echo build cas-server succeed
      BUILD_CAS_SERVER_RESULT=succeed
    else
      echo build cas-server failed
      BUILD_CAS_SERVER_RESULT=failed
    fi
    cd $CURRENT_DIR
  fi
}

function build_all() {
  build_cas_server
  if [ "$SUCCEED" == "$BUILD_CAS_SERVER_RESULT" ]; then
    build_permission
  fi
}

function begin_build() {
  setModuleDir
  case "$1" in
    "-h"|"help") help ;;
    "all") build_all ;;
    "ps") build_permission ;;
    "cas") build_cas_server ;;
    "data") build_dependency ;;
    "client") build_client ;;
    *) echo "错误参数选项: $1" && help;;
  esac
}

if [ $# -eq 0 ]; then
    MVN_MODULE_ARGS="all"
elif [ $# -eq 1 ]; then
    MVN_MODULE_ARGS=$1
else
    help & exit 1
fi

begin_build $MVN_MODULE_ARGS
