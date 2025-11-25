# 工具安装与操作指南（MacOS14.0 M1 Pro环境）

## 1. JDK 1.8 安装与配置

### 1.1 下载JDK 1.8
1. 访问Oracle官方网站：https://www.oracle.com/java/technologies/javase/javase8u211-later-archive-downloads.html
2. 下载适合MacOS的JDK 1.8安装包（dmg格式）
   - 注意：需要Oracle账户登录才能下载
   - 选择文件名类似：`jdk-8uXXX-macosx-x64.dmg`（对于Intel芯片）或`jdk-8uXXX-macosx-aarch64.dmg`（对于M1/M2芯片）

### 1.2 安装JDK 1.8
1. 双击下载的dmg文件，启动安装向导
2. 按照提示完成安装
3. 安装完成后，JDK通常会被安装在：`/Library/Java/JavaVirtualMachines/jdk1.8.0_XXX.jdk/`

### 1.3 配置环境变量
1. 打开终端（Terminal）
2. 输入`nano ~/.zshrc`（MacOS 14默认使用zsh）
3. 在文件末尾添加以下内容：
   ```bash
   export JAVA_HOME="/Library/Java/JavaVirtualMachines/jdk1.8.0_XXX.jdk/Contents/Home"
   export PATH="$JAVA_HOME/bin:$PATH"
   ```
   - 注意：将`XXX`替换为实际的版本号
4. 按下`Control + O`保存文件，然后按下`Control + X`退出编辑器
5. 输入`source ~/.zshrc`使配置生效
6. 验证安装：输入`java -version`，如果显示Java 1.8版本信息，则安装成功

## 2. IntelliJ IDEA 安装与配置

### 2.1 下载IntelliJ IDEA
1. 访问JetBrains官方网站：https://www.jetbrains.com/idea/download/
2. 下载Community版本（免费版）或Ultimate版本（付费版，有30天试用期）

### 2.2 安装IntelliJ IDEA
1. 双击下载的dmg文件
2. 将IntelliJ IDEA图标拖到Applications文件夹中
3. 打开Applications文件夹，双击IntelliJ IDEA启动
4. 首次启动时，选择Do not import settings，然后点击OK
5. 按照提示完成初始设置（主题、插件等）

### 2.3 安装必要的插件
1. 打开IntelliJ IDEA
2. 点击顶部菜单：IntelliJ IDEA → Preferences → Plugins
3. 搜索并安装以下插件：
   - Lombok
   - MyBatisX
   - Spring Assistant
4. 安装完成后重启IDE

## 3. Docker与Docker Compose安装

### 3.1 下载Docker Desktop
1. 访问Docker官方网站：https://www.docker.com/products/docker-desktop/
2. 下载适合MacOS的Docker Desktop安装包

### 3.2 安装Docker Desktop
1. 双击下载的dmg文件
2. 将Docker图标拖到Applications文件夹中
3. 打开Applications文件夹，双击Docker启动
4. 首次启动时，按照提示完成设置
5. 当Docker鲸鱼图标出现在菜单栏且显示绿色时，表示Docker正在运行

### 3.3 验证Docker与Docker Compose安装
1. 打开终端
2. 输入`docker --version`，验证Docker安装
3. 输入`docker-compose --version`，验证Docker Compose安装

## 4. MySQL安装（可选，Docker方式优先）

### 4.1 使用Docker Compose启动MySQL（推荐）
1. 进入项目目录：`cd /Users/djs/Trae/houduanA`
2. 运行命令：`docker-compose up -d`
3. 等待MySQL容器启动
4. 验证MySQL是否正常运行：`docker-compose ps`

### 4.2 直接安装MySQL（备选方案）
1. 访问MySQL官方网站：https://dev.mysql.com/downloads/mysql/
2. 下载适合MacOS的MySQL安装包
3. 双击安装包，按照提示完成安装
4. 安装过程中会提示设置root密码，记好这个密码
5. 安装完成后，可在系统偏好设置中找到MySQL图标，点击启动MySQL服务

## 5. Postman安装与使用

### 5.1 下载Postman
1. 访问Postman官方网站：https://www.postman.com/downloads/
2. 下载适合MacOS的Postman安装包

### 5.2 安装Postman
1. 双击下载的安装包，将Postman图标拖到Applications文件夹中
2. 打开Applications文件夹，双击Postman启动

### 5.3 Postman基本使用
1. 创建工作区：点击Create Workspace
2. 创建集合：点击Collections → + New Collection
3. 添加请求：点击集合 → + Add Request
4. 设置请求方法：GET/POST/PUT/DELETE
5. 输入请求URL，如：http://localhost:8080/tasks
6. 设置请求体（对于POST/PUT请求）：点击Body → raw → JSON
7. 发送请求：点击Send按钮

## 6. 项目运行操作步骤

### 6.1 使用Docker启动MySQL环境
1. 确保Docker Desktop已启动
2. 打开终端，进入项目目录：`cd /Users/djs/Trae/houduanA`
3. 运行命令：`docker-compose up -d`
4. 等待MySQL容器启动成功

### 6.2 导入项目到IntelliJ IDEA
1. 打开IntelliJ IDEA
2. 点击Open或Import Project
3. 选择项目目录：`/Users/djs/Trae/houduanA`
4. 点击OK，选择Maven项目类型
5. 等待IDE加载项目依赖

### 6.3 配置项目
1. 确保JDK设置正确：File → Project Structure → Project → Project SDK选择1.8
2. 检查Maven设置：Preferences → Build, Execution, Deployment → Build Tools → Maven

### 6.4 启动项目
1. 在Project窗口中找到：`src/main/java/com/example/taskmanagement/TaskManagementApiApplication.java`
2. 右键点击该文件，选择Run 'TaskManagementApi...'
3. 等待Spring Boot应用启动
4. 启动成功后，控制台会显示类似信息：`Tomcat started on port(s): 8080 (http)`

### 6.5 访问Swagger文档
1. 打开浏览器
2. 访问地址：http://localhost:8080/swagger-ui.html
3. 可以在Swagger界面查看和测试API

## 7. 项目测试步骤

### 7.1 使用Postman测试API

#### 7.1.1 创建任务
1. 打开Postman
2. 选择POST方法
3. 输入URL：http://localhost:8080/tasks
4. 设置请求体：
   ```json
   {
     "title": "测试任务",
     "description": "这是一个测试任务",
     "completed": false
   }
   ```
5. 点击Send，检查响应

#### 7.1.2 获取所有任务
1. 选择GET方法
2. 输入URL：http://localhost:8080/tasks
3. 点击Send，检查响应

#### 7.1.3 分页查询任务
1. 选择GET方法
2. 输入URL：http://localhost:8080/tasks?page=0&size=10
3. 点击Send，检查响应

#### 7.1.4 搜索任务
1. 选择GET方法
2. 输入URL：http://localhost:8080/tasks?search=测试
3. 点击Send，检查响应

#### 7.1.5 更新任务
1. 选择PUT方法
2. 输入URL：http://localhost:8080/tasks/{id} （替换{id}为实际ID）
3. 设置请求体：
   ```json
   {
     "title": "更新后的任务",
     "description": "更新后的描述",
     "completed": true
   }
   ```
4. 点击Send，检查响应

#### 7.1.6 删除任务
1. 选择DELETE方法
2. 输入URL：http://localhost:8080/tasks/{id} （替换{id}为实际ID）
3. 点击Send，检查响应

#### 7.1.7 批量删除任务
1. 选择DELETE方法
2. 输入URL：http://localhost:8080/tasks/batch
3. 设置请求体：
   ```json
   [1, 2, 3]
   ```
4. 点击Send，检查响应

#### 7.1.8 更新任务状态
1. 选择PATCH方法
2. 输入URL：http://localhost:8080/tasks/{id}/status
3. 设置请求体：
   ```json
   {
     "completed": true
   }
   ```
4. 点击Send，检查响应

### 7.2 运行单元测试
1. 在IntelliJ IDEA中，打开Project窗口
2. 展开`src/test/java`目录
3. 右键点击测试包，选择Run 'All Tests'
4. 等待测试完成，查看测试结果

## 8. 项目检查步骤

### 8.1 代码完整性检查
1. 检查目录结构是否完整：
   - controller/ - 包含TaskController.java
   - service/ - 包含TaskService.java和impl/TaskServiceImpl.java
   - mapper/ - 包含TaskMapper.java
   - model/ - 包含Task.java
   - config/ - 包含MyBatisConfig.java和SwaggerConfig.java
   - resources/mappers/ - 包含TaskMapper.xml
   - resources/ - 包含application.yml和schema.sql

### 8.2 功能检查清单
- [ ] 项目能正常启动
- [ ] MySQL连接正常
- [ ] 能通过Swagger文档访问API
- [ ] 能创建任务
- [ ] 能查询任务列表
- [ ] 能分页查询任务
- [ ] 能搜索任务
- [ ] 能更新任务
- [ ] 能删除任务
- [ ] 能批量删除任务
- [ ] 能更新任务状态

### 8.3 单元测试覆盖率检查
1. 在IntelliJ IDEA中运行所有测试
2. 右键点击项目，选择Run 'All Tests with Coverage'
3. 等待测试完成后，查看覆盖率报告
4. 确保覆盖率大于80%

## 9. 常见问题与解决方案

### 9.1 端口占用问题
- 错误信息：`Port 8080 was already in use`
- 解决方案：
  1. 查找占用端口的进程：`lsof -i :8080`
  2. 终止占用端口的进程：`kill -9 进程ID`
  3. 或者修改application.yml中的端口配置

### 9.2 数据库连接失败
- 错误信息：`Could not create connection to database server`
- 解决方案：
  1. 确保MySQL服务已启动：`docker-compose ps`
  2. 检查数据库配置是否正确（用户名、密码、数据库名）
  3. 检查MySQL容器是否正常运行

### 9.3 Maven依赖下载失败
- 错误信息：`Could not resolve dependencies`
- 解决方案：
  1. 刷新Maven依赖：右键项目 → Maven → Reload Project
  2. 检查网络连接
  3. 考虑配置Maven镜像源

### 9.4 Docker权限问题
- 错误信息：`permission denied`
- 解决方案：
  1. 确保用户加入了docker组
  2. 或者使用sudo权限运行Docker命令（不推荐）

## 10. 项目推送到GitHub

### 10.1 创建GitHub仓库
1. 访问GitHub官网：https://github.com/
2. 登录GitHub账户
3. 点击右上角的+号，选择New repository
4. 填写仓库名称，点击Create repository

### 10.2 初始化本地Git仓库
1. 打开终端，进入项目目录
2. 运行命令：`git init`
3. 运行命令：`git add .`
4. 运行命令：`git commit -m "Initial commit"`

### 10.3 关联远程仓库
1. 在GitHub仓库页面，复制仓库的HTTPS或SSH地址
2. 在终端中运行：`git remote add origin 仓库地址`
3. 运行命令：`git push -u origin main`
4. 按照提示输入GitHub用户名和密码或SSH密钥

### 10.4 提交更新
1. 修改代码后，运行命令：`git add .`
2. 运行命令：`git commit -m "更新说明"`
3. 运行命令：`git push`

## 11. 日常开发流程

### 11.1 启动开发环境
1. 启动Docker Desktop
2. 进入项目目录，运行：`docker-compose up -d`
3. 在IntelliJ IDEA中启动Spring Boot应用

### 11.2 代码开发流程
1. 编写代码
2. 运行单元测试
3. 手动测试API功能
4. 提交代码到GitHub

### 11.3 关闭开发环境
1. 在IntelliJ IDEA中停止应用
2. 进入项目目录，运行：`docker-compose down`
3. 关闭Docker Desktop（可选）