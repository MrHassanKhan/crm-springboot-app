import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { ActivityLogDTO } from 'app/activity-log/activity-log.model';


@Injectable({
  providedIn: 'root',
})
export class ActivityLogService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/activityLogs';

  getAllActivityLogs() {
    return this.http.get<ActivityLogDTO[]>(this.resourcePath);
  }

  getActivityLog(id: number) {
    return this.http.get<ActivityLogDTO>(this.resourcePath + '/' + id);
  }

  createActivityLog(activityLogDTO: ActivityLogDTO) {
    return this.http.post<number>(this.resourcePath, activityLogDTO);
  }

  updateActivityLog(id: number, activityLogDTO: ActivityLogDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, activityLogDTO);
  }

  deleteActivityLog(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

}
