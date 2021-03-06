package com.gizwits.lease.manager.dto;

import com.gizwits.lease.enums.StatusType;
import com.gizwits.lease.manager.entity.Operator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

/**
 * Dto - 运营商列表
 *
 * @author lilh
 * @date 2017/7/31 18:26
 */
public class OperatorForListDto {

    private Integer id;

    private String name;

    private String province;

    private String city;

    private String area;

    /** province + city + area */
    private String region;

    private Integer status;

    private String statusDesc;

    /** 投放点数量 */
    private Integer launchAreaCount = 0;

    /** 设备数量 */
    private Integer deviceCount = 0;

    public OperatorForListDto(Operator operator) {
        BeanUtils.copyProperties(operator, this);
        this.statusDesc = StatusType.getDesc(this.getStatus());
        this.region = this.province + "/" + this.city + "/" + StringUtils.defaultIfEmpty(area, "");
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public Integer getLaunchAreaCount() {
        return launchAreaCount;
    }

    public void setLaunchAreaCount(Integer launchAreaCount) {
        this.launchAreaCount = launchAreaCount;
    }

    public Integer getDeviceCount() {
        return deviceCount;
    }

    public void setDeviceCount(Integer deviceCount) {
        this.deviceCount = deviceCount;
    }
}
