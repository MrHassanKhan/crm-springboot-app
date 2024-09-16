import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { SalesOpportunityDTO } from 'app/sales-opportunity/sales-opportunity.model';
import { map } from 'rxjs';
import { transformRecordToMap } from 'app/common/utils';


@Injectable({
  providedIn: 'root',
})
export class SalesOpportunityService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/salesOpportunities';

  getAllSalesOpportunities() {
    return this.http.get<SalesOpportunityDTO[]>(this.resourcePath);
  }

  getSalesOpportunity(id: number) {
    return this.http.get<SalesOpportunityDTO>(this.resourcePath + '/' + id);
  }

  createSalesOpportunity(salesOpportunityDTO: SalesOpportunityDTO) {
    return this.http.post<number>(this.resourcePath, salesOpportunityDTO);
  }

  updateSalesOpportunity(id: number, salesOpportunityDTO: SalesOpportunityDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, salesOpportunityDTO);
  }

  deleteSalesOpportunity(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

  getLeadValues() {
    return this.http.get<Record<string,string>>(this.resourcePath + '/leadValues')
        .pipe(map(transformRecordToMap));
  }

  getAssignedToValues() {
    return this.http.get<Record<string,string>>(this.resourcePath + '/assignedToValues')
        .pipe(map(transformRecordToMap));
  }

}
