import React, {useState, useEffect} from 'react';
import {Radio, Spin, Table} from 'antd';
import moment from "moment";

// css
import './index.scss';

// service
import logService from 'service/logService';

// init const
const getInitTimeRangeList = () => {
    const todayStartTime = moment().format('YYYY-MM-DD') + ' 00:00:00';
    const nowTime = moment().format('YYYY-MM-DD HH:mm:ss');
    const threeDaysAgoTime = moment().subtract('days', 2).format('YYYY-MM-DD HH:mm:ss');
    const sevenDaysAgoTime = moment().subtract('days', 6).format('YYYY-MM-DD HH:mm:ss');
    return [
        {label: '今天', value: [todayStartTime, nowTime]},
        {label: '三天', value: [threeDaysAgoTime, nowTime]},
        {label: '七天', value: [sevenDaysAgoTime, nowTime]},
    ];
};

const Overview = () => {

    const [spinning, setSpinning] = useState(false);

    const [filterForm, setFilterForm] = useState({
        projectIdentifier: 'testProject',
        startTime: '',
        endTime: '',
    });

    /**
     * 获取总览页数据
     *
     * @param startTime
     * @param endTime
     */
    const getOverallByTimeRange = (startTime, endTime) => {
        setSpinning(true);
        logService.getOverallByTimeRange({startTime, endTime})
            .then(res => {
                setSpinning(false);
                console.log('[成功]获取总览页信息', res);
            })
            .catch(err => {
                console.log('[失败]获取总览页信息', err);
                setSpinning(false);
            });
    };

    /**
     * 获取日志统计数据
     *
     * @param formData
     */
    const getLogCountByHours = async formData => {
        setSpinning(true);
        // 获取jsErrorLog统计信息
        let jsErrorLog, httpErrorLog, resourceLoadErrorLog, customErrorLog;
        try {
            const res = await logService.getLogCountByHours({...formData, logType: 'jsErrorLog'});
            if (res && res.success) {
                jsErrorLog = res.data;
                console.log('[成功]获取jsErrorLog统计信息', jsErrorLog);
            } else {
                console.log('[错误]获取jsErrorLog统计信息', res);
            }
        } catch (e) {
            console.log('[错误]获取jsErrorLog统计信息', e);
        }
        // 获取httpErrorLog统计信息
        try {
            const res = await logService.getLogCountByHours({...formData, logType: 'httpErrorLog'});
            if (res && res.success) {
                httpErrorLog = res.data;
                console.log('[成功]获取httpErrorLog统计信息', httpErrorLog);
            } else {
                console.log('[错误]获取httpErrorLog统计信息', res);
            }
        } catch (e) {
            console.log('[错误]获取httpErrorLog统计信息', e);
        }
        // 获取resourceLoadErrorLog统计信息
        try {
            const res = await logService.getLogCountByHours({...formData, logType: 'resourceLoadErrorLog'});
            if (res && res.success) {
                resourceLoadErrorLog = res.data;
                console.log('[成功]获取resourceLoadErrorLog统计信息', resourceLoadErrorLog);
            } else {
                console.log('[错误]获取resourceLoadErrorLog统计信息', res);
            }
        } catch (e) {
            console.log('[错误]获取resourceLoadErrorLog统计信息', e);
        }
        // 获取customErrorLog统计信息
        try {
            const res = await logService.getLogCountByHours({...formData, logType: 'customErrorLog'});
            if (res && res.success) {
                customErrorLog = res.data;
                console.log('[成功]获取customErrorLog统计信息', customErrorLog);
            } else {
                console.log('[错误]获取customErrorLog统计信息', res);
            }
        } catch (e) {
            console.log('[错误]获取customErrorLog统计信息', e);
        }
        setSpinning(false);
    };

    useEffect(() => {
        getOverallByTimeRange(filterForm.startTime, filterForm.endTime);
        getLogCountByHours(filterForm);
    }, [filterForm]);

    const [timeRangeList, setTimeRangeList] = useState(getInitTimeRangeList());

    return (
        <div className='Overview-container'>
            <div className='Overview-topBanner'>
                <div className='Overview-topBanner-input'>
                    <Radio.Group defaultValue={timeRangeList[0].value} buttonStyle="solid"
                                 onChange={e => {
                                     const timeList = e.target.value;
                                     setFilterForm({...filterForm, startTime: timeList[0], endTime: timeList[1]});
                                 }}>
                        {timeRangeList.map(item => (
                            <Radio.Button value={item.value} key={item.label}>{item.label}</Radio.Button>
                        ))}
                    </Radio.Group>
                </div>
            </div>
            <Spin spinning={spinning}>

            </Spin>
        </div>
    );
};

export default Overview;