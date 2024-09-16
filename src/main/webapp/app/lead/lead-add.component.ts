import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { LeadService } from 'app/lead/lead.service';
import { LeadDTO } from 'app/lead/lead.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';


@Component({
  selector: 'app-lead-add',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './lead-add.component.html'
})
export class LeadAddComponent implements OnInit {

  leadService = inject(LeadService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  addForm = new FormGroup({
    firstname: new FormControl(null, [Validators.maxLength(255)]),
    lastname: new FormControl(null, [Validators.maxLength(255)]),
    email: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    phone: new FormControl(null, [Validators.maxLength(255)]),
    status: new FormControl(null)
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@lead.create.success:Lead was created successfully.`,
      LEAD_EMAIL_UNIQUE: $localize`:@@Exists.lead.email:This Email is already taken.`
    };
    return messages[key];
  }

  ngOnInit() {

  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new LeadDTO(this.addForm.value);
    this.leadService.createLead(data)
        .subscribe({
          next: () => this.router.navigate(['/leads'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
