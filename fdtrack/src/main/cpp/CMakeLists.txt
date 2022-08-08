# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.18.1)

# Declares and names the project.

project("fdtrack")

set(UNWINDSTACK_ROOT ${CMAKE_CURRENT_SOURCE_DIR}/unwindstack/)

set(FDTRACK_ROOT ${CMAKE_CURRENT_SOURCE_DIR}/fdtrack/)

include_directories(${CMAKE_CURRENT_SOURCE_DIR}/include ${UNWINDSTACK_ROOT}/include)

set(ANDROID_SOURCES
        ${CMAKE_CURRENT_SOURCE_DIR}/android-base/file.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/android-base/stringprintf.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/android-base/strings.cpp)

set(UNWINDSTACK_SOURCES
        ${UNWINDSTACK_ROOT}/ArmExidx.cpp
        ${UNWINDSTACK_ROOT}/ArmExidx.h
        ${UNWINDSTACK_ROOT}/Check.h
        #${UNWINDSTACK_ROOT}/DexFile.cpp
        #${UNWINDSTACK_ROOT}/DexFile.h
        ${UNWINDSTACK_ROOT}/DexFiles.cpp
        ${UNWINDSTACK_ROOT}/DwarfCfa.cpp
        ${UNWINDSTACK_ROOT}/DwarfCfa.h
        ${UNWINDSTACK_ROOT}/DwarfDebugFrame.h
        ${UNWINDSTACK_ROOT}/DwarfEhFrame.h
        ${UNWINDSTACK_ROOT}/DwarfEhFrameWithHdr.cpp
        ${UNWINDSTACK_ROOT}/DwarfEhFrameWithHdr.h
        ${UNWINDSTACK_ROOT}/DwarfEncoding.h
        ${UNWINDSTACK_ROOT}/DwarfMemory.cpp
        ${UNWINDSTACK_ROOT}/DwarfOp.cpp
        ${UNWINDSTACK_ROOT}/DwarfOp.h
        ${UNWINDSTACK_ROOT}/DwarfSection.cpp
        ${UNWINDSTACK_ROOT}/Elf.cpp
        ${UNWINDSTACK_ROOT}/ElfInterface.cpp
        ${UNWINDSTACK_ROOT}/ElfInterfaceArm.cpp
        ${UNWINDSTACK_ROOT}/ElfInterfaceArm.h
        ${UNWINDSTACK_ROOT}/Global.cpp
        ${UNWINDSTACK_ROOT}/JitDebug.cpp
        ${UNWINDSTACK_ROOT}/LocalUnwinder.cpp
        ${UNWINDSTACK_ROOT}/Log.cpp
        ${UNWINDSTACK_ROOT}/MapInfo.cpp
        ${UNWINDSTACK_ROOT}/Maps.cpp
        ${UNWINDSTACK_ROOT}/Memory.cpp
        ${UNWINDSTACK_ROOT}/MemoryBuffer.h
        ${UNWINDSTACK_ROOT}/MemoryCache.h
        ${UNWINDSTACK_ROOT}/MemoryFileAtOffset.h
        ${UNWINDSTACK_ROOT}/MemoryLocal.h
        ${UNWINDSTACK_ROOT}/MemoryMte.cpp
        ${UNWINDSTACK_ROOT}/MemoryOffline.h
        ${UNWINDSTACK_ROOT}/MemoryOfflineBuffer.h
        ${UNWINDSTACK_ROOT}/MemoryRange.h
        ${UNWINDSTACK_ROOT}/MemoryRemote.h
        ${UNWINDSTACK_ROOT}/Regs.cpp
        ${UNWINDSTACK_ROOT}/RegsArm.cpp
        ${UNWINDSTACK_ROOT}/RegsArm64.cpp
        ${UNWINDSTACK_ROOT}/RegsInfo.h
        ${UNWINDSTACK_ROOT}/RegsX86_64.cpp
        ${UNWINDSTACK_ROOT}/RegsX86.cpp
        ${UNWINDSTACK_ROOT}/Symbols.cpp
        ${UNWINDSTACK_ROOT}/Symbols.h
        ${UNWINDSTACK_ROOT}/ThreadEntry.cpp
        ${UNWINDSTACK_ROOT}/ThreadEntry.h
        ${UNWINDSTACK_ROOT}/ThreadUnwinder.cpp
        ${UNWINDSTACK_ROOT}/unistdfix.h
        ${UNWINDSTACK_ROOT}/Unwinder.cpp)

if(${CMAKE_SYSTEM_PROCESSOR} MATCHES arm)
elseif(${CMAKE_SYSTEM_PROCESSOR} MATCHES "amd64.*|x86_64.*|AMD64.*")
    set(UNWINDSTACK_SOURCES_GETREG
            ${UNWINDSTACK_ROOT}/AsmGetRegsX86_64.S
            )
elseif(${CMAKE_SYSTEM_PROCESSOR} MATCHES "i686.*|i386.*|x86.*")
    set(UNWINDSTACK_SOURCES_GETREG
            ${UNWINDSTACK_ROOT}/AsmGetRegsX86.S
            )
else()
    add_definitions(-DEM_ARM=40)
endif()

add_library(unwindstack STATIC
        ${ANDROID_SOURCES}
        ${UNWINDSTACK_SOURCES}
        ${UNWINDSTACK_SOURCES_GETREG})

set_property(TARGET unwindstack PROPERTY CXX_STANDARD 17)

if(CMAKE_CXX_COMPILER_ID MATCHES "Clang")
    target_compile_options(unwindstack PRIVATE $<BUILD_INTERFACE:-Wno-unknown-attributes>)
endif()


# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
        fdtrack
        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        fdtrack/fdtrack.cpp fdtrack/fdtrack_run.cpp)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib
        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)


find_library( # Sets the name of the path variable.
        libc_bionic_ndk
        # Specifies the name of the NDK library that
        # you want CMake to locate.
        c)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        fdtrack
        unwindstack
        ${libc_bionic_ndk}
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})