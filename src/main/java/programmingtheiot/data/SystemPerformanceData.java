/**
 * This class is part of the Programming the Internet of Things project.
 * 
 * It is provided as a simple shell to guide the student and assist with
 * implementation for the Programming the Internet of Things exercises,
 * and designed to be modified by the student as needed.
 */ 

package programmingtheiot.data;

import java.io.Serializable;

import programmingtheiot.common.ConfigConst;

/**
 * Shell representation of class for student implementation.
 *
 */
public class SystemPerformanceData extends BaseIotData implements Serializable
{
    // NOTE: You should create your own unique serialVersionUID
    private static final long serialVersionUID = 1L;

    // private var's
    private float cpuUtil  = ConfigConst.DEFAULT_VAL;
    private float diskUtil = ConfigConst.DEFAULT_VAL;
    private float memUtil  = ConfigConst.DEFAULT_VAL;

    // constructors
    public SystemPerformanceData()
    {
        super();
        super.setName(ConfigConst.SYS_PERF_DATA);
    }

    // public methods
    public float getCpuUtilization()
    {
        return this.cpuUtil;
    }

    public void setCpuUtilization(float cpuUtil)
    {
        super.updateTimeStamp();
        this.cpuUtil = cpuUtil;
    }

    public float getDiskUtilization()
    {
        return this.diskUtil;
    }

    public void setDiskUtilization(float diskUtil)
    {
        super.updateTimeStamp();
        this.diskUtil = diskUtil;
    }

    public float getMemoryUtilization()
    {
        return this.memUtil;
    }

    public void setMemoryUtilization(float memUtil)
    {
        super.updateTimeStamp();
        this.memUtil = memUtil;
    }

    // protected methods
    @Override
    protected void handleUpdateData(BaseIotData data)
    {
        if (data instanceof SystemPerformanceData) {
            SystemPerformanceData spd = (SystemPerformanceData) data;
            this.setCpuUtilization(spd.getCpuUtilization());
            this.setDiskUtilization(spd.getDiskUtilization());
            this.setMemoryUtilization(spd.getMemoryUtilization());
        }
    }

    /**
     * Returns a string representation of this instance. This will invoke the base class
     * {@link #toString()} method, then append the output from this call.
     * 
     * @return String The string representing this instance, returned in CSV 'key=value' format.
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(super.toString());
        
        sb.append(',');
        sb.append(ConfigConst.CPU_UTIL_PROP).append('=').append(this.getCpuUtilization()).append(',');
        sb.append(ConfigConst.DISK_UTIL_PROP).append('=').append(this.getDiskUtilization()).append(',');
        sb.append(ConfigConst.MEM_UTIL_PROP).append('=').append(this.getMemoryUtilization());
        
        return sb.toString();
    }
}
