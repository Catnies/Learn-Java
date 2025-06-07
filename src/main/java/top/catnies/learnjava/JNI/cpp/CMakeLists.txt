# cmake -G "MinGW Makefiles" -> 根据 CmakeLists 生成 MakeFiles .
# mingw32-make ->  根据 MakeFiles 去生成最终的文件.
# Cmake -> 对应操作系统的MakeFiles -> 使用Make/ninja调用编译器进行构建

# Cmake的最低适配版本
cmake_minimum_required(VERSION 3.31)

# 项目名称, 项目版本, 使用语言...
project(SimpleJNI VERSION 0.1.0 LANGUAGES CXX)

# 查找 Java 开发环境 (JDK)
find_package(Java REQUIRED Development)  # "Development" 表示需要 JDK
find_package(JNI REQUIRED)                # 自动查找 JNI 头文件和库

# 创建库
add_library(simplejni SHARED top_catnies_simplelib_SimpleLibSource.cpp) # 创建动态库（SHARED）目标

# 目标文件夹
set_target_properties(simplejni PROPERTIES
    RUNTIME_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/bin
    LIBRARY_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/lib
    ARCHIVE_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/lib
)

# 包含 JNI 头文件路径
target_include_directories(simplejni PRIVATE ${JNI_INCLUDE_DIRS})

# 链接 JNI 库（在 Windows 上通常是 jvm.lib，在 Linux/macOS 上是 jvm）
target_link_libraries(simplejni PRIVATE ${JNI_LIBRARIES})