import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { CustomerService } from 'app/customer/customer.service';
import { CustomerRequestDTO } from 'app/customer/customer.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';


@Component({
  selector: 'app-customer-add',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './customer-add.component.html'
})
export class CustomerAddComponent implements OnInit {

  customerService = inject(CustomerService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  // assignedSalesPersonValues?: Map<number,string>;

  addForm = new FormGroup({
    firstName: new FormControl(null, [Validators.maxLength(255)]),
    lastName: new FormControl(null, [Validators.maxLength(255)]),
    email: new FormControl(null, [Validators.maxLength(255)]),
    phone: new FormControl(null, [Validators.maxLength(255)]),
    address: new FormControl(null, [Validators.maxLength(255)]),
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@customer.create.success:Customer was created successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
    // this.customerService.getAssignedSalesPersonValues()
    //     .subscribe({
    //       next: (data) => this.assignedSalesPersonValues = data,
    //       error: (error) => this.errorHandler.handleServerError(error.error)
    //     });
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new CustomerRequestDTO(this.addForm.value);
    this.customerService.createCustomer(data)
        .subscribe({
          next: () => this.router.navigate(['/customers'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
