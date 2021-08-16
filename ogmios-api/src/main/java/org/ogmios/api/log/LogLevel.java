package org.ogmios.api.log;

public enum LogLevel {
    ERROR(5), WARN(4), INFO(3), DEBUG(2), TRACE(1);

    private LogLevel(int no){
        this.no=no;
    }

    private final int no;

    public int getNo(){
        return no;
    }

    public static final LogLevel DEFAULT=ERROR;

    public static LogLevel get(String s){
        for(LogLevel logLevel:LogLevel.values()){
            if(logLevel.equals(s)){
                return logLevel;
            }
        }
        return null;
    }
}
