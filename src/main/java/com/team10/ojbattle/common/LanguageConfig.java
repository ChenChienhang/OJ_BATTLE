package com.team10.ojbattle.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alibaba.druid.support.monitor.annotation.AggregateType.None;

/**
 * @author: 陈健航
 * @description:
 * @since: 2020/6/19 22:09
 * @version: 1.0
 */
public class LanguageConfig {

    /**
     * c_spj
     */
    public final static Map<String, Object> C_LANG_SPJ_CONFIG = new HashMap<String, Object>() {{
        put("exe_name", "spj-{spj_version}");
        put("command", "{exe_path} {in_file_path} {user_out_file_path}");
        put("seccomp_rule", "c_cpp");
    }};

    /**
     * default_env
     */
    private final static List<String> DEFAULT_ENV = new ArrayList<String>() {
        {
            add("LANG=en_US.UTF-8");
            add("LANGUAGE=en_US:en");
            add("LC_ALL=en_US.UTF-8");
        }
    };

    /**
     * java
     */
    public final static Map<String, Object> JAVA_LANG_CONFIG = new HashMap<String, Object>() {
        {
            put("name", "java");
            put("compile", new HashMap<String, Object>() {
                {
                    put("src_name", "Main.java");
                    put("exe_name", "Main");
                    put("max_cpu_time", 3000);
                    put("max_real_time", 5000);
                    put("max_memory", -1);
                    put("compile_command", "/usr/bin/javac {src_path} -d {exe_dir} -encoding UTF8");
                }
            });
            put("run", new HashMap<String, Object>() {
                {
                    put("command", "/usr/bin/java -cp {exe_dir} -XX:MaxRAM={max_memory}k -Djava.security.manager -Dfile.encoding=UTF-8 -Djava.security.policy==/etc/java_policy -Djava.awt.headless=true Main");
                    put("seccomp_rule", None);
                    put("env", DEFAULT_ENV);
                    put("memory_limit_check_only", 1);
                }
            });
        }
    };

    /**
     * py2
     */
    public final static Map<String, Object> PY2_LANG_CONFIG = new HashMap<String, Object>() {
        {
            put("compile", new HashMap<String, Object>() {
                {
                    put("src_name", "solution.py");
                    put("exe_name", "solution.pyc");
                    put("max_cpu_time", 3000);
                    put("max_real_time", 5000);
                    put("max_memory", 128 * 1024 * 1024);
                    put("compile_command", "/usr/bin/python -m py_compile {src_path}");
                }
            });
            put("run", new HashMap<String, Object>() {
                {
                    put("command", "/usr/bin/python {exe_path}");
                    put("seccomp_rule", "general");
                    put("env", DEFAULT_ENV);
                }
            });
        }
    };

    /**
     * py3
     */
    public final static Map<String, Object> PY3_LANG_CONFIG = new HashMap<String, Object>() {
        {
            put("compile", new HashMap<String, Object>() {
                {
                    put("src_name", "solution.py");
                    put("exe_name", "__pycache__/solution.cpython-35.pyc");
                    put("max_cpu_time", 3000);
                    put("max_real_time", 5000);
                    put("max_memory", 128 * 1024 * 1024);
                    put("compile_command", "/usr/bin/python3 -m py_compile {src_path}");
                }
            });
            put("run", new HashMap<String, Object>() {
                {

                    put("command", "/usr/bin/python3 {exe_path}");
                    put("seccomp_rule", "general");
                    put("env", new ArrayList<String>() {
                        {
                            add("PYTHONIOENCODING=UTF-8");
                            addAll(DEFAULT_ENV);
                        }
                    });
                }
            });
        }
    };

    /**
     * c
     */
    public final static Map<String, Object> C_LANG_CONFIG = new HashMap<String, Object>() {
        {
            put("compile", new HashMap<String, Object>() {
                        {
                            put("src_name", "main.c");
                            put("exe_name", "main");
                            put("max_cpu_time", 3000);
                            put("max_real_time", 5000);
                            put("max_memory", 128 * 1024 * 1024);
                            put("compile_command", "/usr/bin/gcc -DONLINE_JUDGE -O2 -w -fmax-errors=3 -std=c99 {src_path} -lm -o {exe_path}");
                        }
                    }
            );
            put("run", new HashMap<String, Object>() {
                {
                    put("command", "{exe_path}");
                    put("seccomp_rule", "c_cpp");
                    put("env", DEFAULT_ENV);
                }
            });
        }
    };

    /**
     * cpp
     */
    public final static Map<String, Object> CPP_LANG_CONFIG = new HashMap<String, Object>() {
        {
            put("compile", new HashMap<String, Object>() {
                {
                    put("src_name", "main.cpp");
                    put("exe_name", "main");
                    put("max_cpu_time", 3000);
                    put("max_real_time", 5000);
                    put("max_memory", 128 * 1024 * 1024);
                    put("compile_command", "/usr/bin/g++ -DONLINE_JUDGE -O2 -w -fmax-errors=3 -std=c++11 {src_path} -lm -o {exe_path}");
                }

            });
            put("run", new HashMap<String, Object>() {
                {
                    put("command", "{exe_path}");
                    put("seccomp_rule", "c_cpp");
                    put("env", DEFAULT_ENV);
                }
            });
        }
    };
}
