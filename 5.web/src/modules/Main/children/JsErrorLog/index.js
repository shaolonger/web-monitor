import React, {useState, useEffect} from 'react';
import {DatePicker, Select, Table, Button, message, Spin, Radio} from 'antd';
import moment from "moment";
import echarts from 'echarts';

// css
import './index.scss';

// service
import logService from 'service/logService';

// init const
// 总览表单查询参数
const overallDays = 14;
const overallFilterForm = {
    logType: 'jsErrorLog',
    projectIdentifier: 'testProject',
    startTime: moment().subtract('days', overallDays).format('YYYY-MM-DD'),
    endTime: moment().format('YYYY-MM-DD'),
};
// 图表实例
const chartInstance = {
    jsErrorLogChart: null,
};
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

const JsErrorLog = () => {

    // 统计图表
    const [jsErrorLogChartData, setJsErrorLogChartData] = useState({});
    useEffect(() => {
        getLogCountByDays();
    }, []);
    useEffect(() => {
        initOrUpdateChart('jsErrorLogChart', jsErrorLogChartData);
    }, [jsErrorLogChartData]);

    // 筛选条件
    const [timeRangeList, setTimeRangeList] = useState(getInitTimeRangeList());
    const [filterForm, setFilterForm] = useState({
        pageNum: 1,
        pageSize: 10,
        startTime: timeRangeList[0].value[0],
        endTime: timeRangeList[0].value[1],
    });
    const [total, setTotal] = useState(3);
    const [spinning, setSpinning] = useState(false);

    useEffect(() => {
        // console.log('[useCallback]filterForm', filterForm);
        getTableList(filterForm);
    }, [filterForm]);

    // 表格数据
    const [dataSource, setDataSource] = useState([]);
    const columns = [
        {
            title: '序号',
            dataIndex: 'index',
            key: 'index',
        },
        {
            title: '错误类型',
            dataIndex: 'errorType',
            key: 'errorType',
        },
        {
            title: '错误信息',
            dataIndex: 'errorMessage',
            key: 'errorMessage',
        },
        {
            title: '页面地址',
            dataIndex: 'pageUrl',
            key: 'pageUrl',
        },
        {
            title: '设备',
            dataIndex: 'deviceName',
            key: 'deviceName',
        },
        {
            title: '用户',
            dataIndex: 'userName',
            key: 'userName',
        },
        {
            title: '发生时间',
            dataIndex: 'createTime',
            key: 'createTime',
        },
    ];

    /**
     * 获取日志统计数据
     *
     * @param formData
     */
    const getLogCountByDays = async formData => {
        setSpinning(true);
        // 获取jsErrorLog统计信息
        let jsErrorLog;
        try {
            const res = await logService.getLogCountByDays(overallFilterForm);
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
        setSpinning(false);
    };

    /**
     * 初始化或更新图表
     *
     * @param id
     * @param chartData
     */
    const initOrUpdateChart = (id, chartData) => {
        if (!chartData) return;
        if (!chartInstance[id]) {
            chartInstance[id] = echarts.init(document.getElementById(id));
        }
        const option = {
            xAxis: {
                type: 'category',
                data: Object.keys(chartData)
            },
            yAxis: {
                type: 'value'
            },
            series: [{
                data: Object.values(chartData),
                type: 'bar',
                barWidth: 30
            }],
            grid: {
                left: '0%',
                right: '0%',
                top: '0%',
                bottom: '20px'
            },
        };
        chartInstance[id].setOption(option);
    };

    /**
     * 获取列表数据
     * @param form
     */
    const getTableList = form => {
        setSpinning(true);
        logService.getJsErrorLog(form)
            .then(res => {
                setSpinning(false);
                console.log('[成功]查询jsErrorLog列表数据', res);
                const {success, data, msg} = res;
                if (!success) {
                    message.error({content: msg || '查询jsErrorLog列表数据失败'});
                } else {
                    const {records, totalNum} = data;
                    setTotal(totalNum);
                    const dataSource = records.map((item, index) => ({
                        key: index + 1,
                        index: index + 1,
                        errorType: item.errorType,
                        errorMessage: item.errorMessage,
                        pageUrl: item.pageUrl,
                        deviceName: item.deviceName,
                        userName: item.userName,
                        createTime: moment(new Date(item.createTime)).format('YYYY-MM-DD HH:mm:ss'),
                    }));
                    setDataSource(dataSource);
                }
            })
            .catch(err => {
                setSpinning(false);
                console.log('[失败]查询jsErrorLog列表数据', err);
                message.error({content: err.msg || '查询jsErrorLog列表数据失败'});
            });
    };

    const onChange = (pageNum, pageSize) => {
        // console.log(pageNum, pageSize);
        setFilterForm({...filterForm, pageNum, pageSize});
    };
    const onShowSizeChange = (pageNum, pageSize) => {
        // console.log(pageNum, pageSize);
        setFilterForm({...filterForm, pageNum, pageSize});
    };
    const pagination = {
        showSizeChanger: true,
        current: filterForm.pageNum,
        total,
        onShowSizeChange,
        onChange
    };

    return (
        <div className='jsError-container'>
            <div className='jsError-topBanner'>
                <div className='jsError-topBanner-radio'>
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
                <div className='jsError-topBanner-datePicker'>
                    <DatePicker.RangePicker
                        showTime format='YYYY-MM-DD hh:mm:ss' allowClear={false}
                        value={[moment(filterForm.startTime), moment(filterForm.endTime)]}
                        onChange={
                            (date, dateString) => {
                                setFilterForm({...filterForm, startTime: dateString[0], endTime: dateString[1]})
                            }
                        }
                    />
                </div>
            </div>
            <ul className='jsError-charts'>
                <div id='jsErrorLogChart' className='chartItem'></div>
            </ul>
            <div className='jsError-table'>
                <Spin spinning={spinning}>
                    <Table dataSource={dataSource} columns={columns} pagination={pagination}/>
                </Spin>
            </div>
        </div>
    );
};

export default JsErrorLog;