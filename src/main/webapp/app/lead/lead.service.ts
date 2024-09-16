import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { LeadDTO } from 'app/lead/lead.model';
import { map } from 'rxjs';
import { transformRecordToMap } from 'app/common/utils';


@Injectable({
  providedIn: 'root',
})
export class LeadService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/leads';

  getAllLeads() {
    return this.http.get<LeadDTO[]>(this.resourcePath);
  }

  getLead(id: number) {
    return this.http.get<LeadDTO>(this.resourcePath + '/' + id);
  }

  createLead(leadDTO: LeadDTO) {
    return this.http.post<number>(this.resourcePath, leadDTO);
  }

  updateLead(id: number, leadDTO: LeadDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, leadDTO);
  }

  deleteLead(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

  getAssignedUserValues() {
    return this.http.get<Record<string,string>>(this.resourcePath + '/assignedUserValues')
        .pipe(map(transformRecordToMap));
  }

}
