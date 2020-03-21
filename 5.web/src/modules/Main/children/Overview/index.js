import React, {useState, useEffect} from 'react';
import {Radio, Spin, Table} from 'antd';
import moment from "moment";
import echarts from 'echarts';

// css
import './index.scss';

// service
import logService from 'service/logService';

// init const
const getInitTimeRangeList = () => {
    const todayStartTime = moment().format('YYYY-MM-DD') + ' 00:00:00';
    const nowTime = moment().format('YYYY-MM-DD HH:mm:ss');
    const threeDaysAgoTime = moment().subtract(2, 'days').format('YYYY-MM-DD HH:mm:ss');
    const sevenDaysAgoTime = moment().subtract(6, 'days').format('YYYY-MM-DD HH:mm:ss');
    return [
        {label: '今天', value: [todayStartTime, nowTime]},
        {label: '三天', value: [threeDaysAgoTime, nowTime]},
        {label: '七天', value: [sevenDaysAgoTime, nowTime]},
    ];
};
const chartTitleMap = {
    'jsErrorLogChart': 'JS错误趋势',
    'httpErrorLogChart': 'HTTP异常趋势',
    'resourceLoadErrorLogChart': '静态资源异常趋势',
    'customErrorLogChart': '自定义异常趋势',
};
// 图表实例
const chartInstance = {
    jsErrorLogChart: null,
    httpErrorLogChart: null,
    resourceLoadErrorLogChart: null,
    customErrorLogChart: null,
};

const Overview = () => {

    const [spinning, setSpinning] = useState(false);

    // 筛选条件
    const [timeRangeList, setTimeRangeList] = useState(getInitTimeRangeList());
    const [filterForm, setFilterForm] = useState({
        projectIdentifier: 'testProject',
        startTime: timeRangeList[0].value[0],
        endTime: timeRangeList[0].value[1],
    });
    useEffect(() => {
        getOverallByTimeRange(filterForm.startTime, filterForm.endTime);
        getLogCountByHours(filterForm);
    }, [filterForm]);

    // 总计
    const [totalCountList, setTotalCountList] = useState([
        {label: 'JS错误', value: 0}, {label: 'HTTP异常', value: 0},
        {label: '静态资源异常', value: 0}, {label: '自定义异常', value: 0},
    ]);

    // 图表数据
    const [jsErrorLogChartData, setJsErrorLogChartData] = useState([]);
    const [httpErrorLogChartData, setHttpErrorLogChartData] = useState([]);
    const [resourceLoadErrorLogChartData, setResourceLoadErrorLogChartData] = useState([]);
    const [customErrorLogChartData, setCustomErrorLogChartData] = useState([]);
    useEffect(() => {
        initOrUpdateChart('jsErrorLogChart', jsErrorLogChartData);
    }, [jsErrorLogChartData]);
    useEffect(() => {
        initOrUpdateChart('httpErrorLogChart', httpErrorLogChartData);
    }, [httpErrorLogChartData]);
    useEffect(() => {
        initOrUpdateChart('resourceLoadErrorLogChart', resourceLoadErrorLogChartData);
    }, [resourceLoadErrorLogChartData]);
    useEffect(() => {
        initOrUpdateChart('customErrorLogChart', customErrorLogChartData);
    }, [customErrorLogChartData]);

    /**
     * 初始化或更新图表
     *
     * @param id
     * @param chartData
     */
    const initOrUpdateChart = (id, chartData) => {
        if (!chartData || !chartData.ago || !chartData.now) return;
        if (!chartInstance[id]) {
            chartInstance[id] = echarts.init(document.getElementById(id));
        }
        const option = {
            title: {
                text: chartTitleMap[id]
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross',
                    label: {
                        backgroundColor: '#6a7985'
                    }
                }
            },
            legend: {
                data: ['现在', '七天前']
            },
            xAxis: [
                {
                    type: 'category',
                    boundaryGap: false,
                    data: Object.keys(chartData.now)
                }
            ],
            yAxis: [
                {
                    type: 'value'
                }
            ],
            series: [
                {
                    name: '现在',
                    type: 'line',
                    stack: '总量',
                    areaStyle: {},
                    data: Object.values(chartData.now)
                },
                {
                    name: '七天前',
                    type: 'line',
                    stack: '总量',
                    areaStyle: {},
                    data: Object.values(chartData.ago)
                },
            ]
        };
        chartInstance[id].setOption(option);
    };

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
                const {success, data} = res;
                if (!success) {
                    console.log('[失败]获取总览页信息', res);
                } else {
                    console.log('[成功]获取总览页信息', res);
                    const {jsErrorLogCount, httpErrorLogCount, resourceLoadErrorLogCount, customErrorLogCount} = data;
                    setTotalCountList([
                        {label: 'JS错误', value: jsErrorLogCount}, {label: 'HTTP异常', value: httpErrorLogCount},
                        {label: '静态资源异常', value: resourceLoadErrorLogCount}, {label: '自定义异常', value: customErrorLogCount},
                    ]);
                }
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
                setJsErrorLogChartData(jsErrorLog);
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
                setHttpErrorLogChartData(httpErrorLog);
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
                setResourceLoadErrorLogChartData(resourceLoadErrorLog);
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
                setCustomErrorLogChartData(customErrorLog);
            } else {
                console.log('[错误]获取customErrorLog统计信息', res);
            }
        } catch (e) {
            console.log('[错误]获取customErrorLog统计信息', e);
        }
        setSpinning(false);
    };

    return (
        <div className='overview-container'>
            <div className='overview-topBanner'>
                <div className='overview-topBanner-input'>
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
                <div className='overview-content'>
                    <ul className='overview-content-totalList'>
                        {totalCountList.map(item => (
                            <li className='overview-content-totalItem' key={item.label}>
                                <div className='overview-content-round'>
                                    {item.value}
                                </div>
                                <p className='overview-content-text'>{item.label}</p>
                            </li>
                        ))}
                    </ul>
                    <ul className='overview-content-chartsList'>
                        <li className='overview-content-chartsItem'>
                            <div id='jsErrorLogChart' className='chartItem'></div>
                        </li>
                        <li className='overview-content-chartsItem'>
                            <div id='httpErrorLogChart' className='chartItem'></div>
                        </li>
                        <li className='overview-content-chartsItem'>
                            <div id='resourceLoadErrorLogChart' className='chartItem'></div>
                        </li>
                        <li className='overview-content-chartsItem'>
                            <div id='customErrorLogChart' className='chartItem'></div>
                        </li>
                    </ul>
                </div>
            </Spin>
        </div>
    );
};

export default Overview;