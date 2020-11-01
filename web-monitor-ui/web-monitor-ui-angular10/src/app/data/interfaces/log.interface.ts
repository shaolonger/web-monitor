export interface BasicFilters {
    projectIdentifier: string;
    startTime: string;
    endTime: string;
};

export interface LogRecordList {
    count: number;
    latestRecordTime: string;
    errorMessage: string;
    affectUserCount: number;
}