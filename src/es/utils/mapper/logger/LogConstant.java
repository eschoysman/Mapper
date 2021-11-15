package es.utils.mapper.logger;

public class LogConstant {

    public enum TYPE {CREATION, MAPPING}
    public enum LEVEL {
        NONE(0),
        MAPPER(25),
        TYPE(50),
        FIELD(75),
        ALL(100);
        private int prority;
        LEVEL(int priority) {
            this.prority = priority;
        }
        public int getPrority() {
            return this.prority;
        }
    }

    public static final LogConstant CREATION_NONE = new LogConstant(TYPE.CREATION, LEVEL.NONE);
    public static final LogConstant CREATION_LEVEL_MAPPER = new LogConstant(TYPE.CREATION, LEVEL.MAPPER);
    public static final LogConstant CREATION_LEVEL_TYPE = new LogConstant(TYPE.CREATION, LEVEL.TYPE);
    public static final LogConstant CREATION_LEVEL_FIELD = new LogConstant(TYPE.CREATION, LEVEL.FIELD);
    public static final LogConstant CREATION = new LogConstant(TYPE.CREATION, LEVEL.ALL);

    public static final LogConstant MAPPING_NONE = new LogConstant(TYPE.MAPPING, LEVEL.NONE);
    public static final LogConstant MAPPING_LEVEL_MAPPER = new LogConstant(TYPE.MAPPING, LEVEL.MAPPER);
    public static final LogConstant MAPPING_LEVEL_TYPE = new LogConstant(TYPE.MAPPING, LEVEL.TYPE);
    public static final LogConstant MAPPING_LEVEL_FIELD = new LogConstant(TYPE.MAPPING, LEVEL.FIELD);
    public static final LogConstant MAPPING = new LogConstant(TYPE.MAPPING, LEVEL.ALL);

    private TYPE type;
    private LEVEL level;

    private LogConstant(TYPE type, LEVEL level) {
        this.type = type;
        this.level = level;
    }

    public TYPE getType() {
        return this.type;
    }

    public LEVEL getLevel() {
        return this.level;
    }

    public String toString() {
        return "LogConstant[type:"+getType()+", level:"+getLevel()+"]";
    }

}
