import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import {CustomerRequestDTO, CustomerResponseDTO} from 'app/customer/customer.model';
import { map } from 'rxjs';
import { transformRecordToMap } from 'app/common/utils';


@Injectable({
  providedIn: 'root',
})
export class CustomerService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/customers';

  getAllCustomers() {
    return this.http.get<CustomerResponseDTO[]>(this.resourcePath);
  }

  getCustomer(id: number) {
    return this.http.get<CustomerResponseDTO>(this.resourcePath + '/' + id);
  }

  createCustomer(customerDTO: CustomerRequestDTO) {
    return this.http.post<number>(this.resourcePath, customerDTO);
  }

  updateCustomer(id: number, customerDTO: CustomerRequestDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, customerDTO);
  }

  deleteCustomer(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

  getAssignedSalesPersonValues() {
    return this.http.get<Record<string,string>>(this.resourcePath + '/assignedSalesPersonValues')
        .pipe(map(transformRecordToMap));
  }

}
