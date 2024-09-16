import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { ActivityLogService } from 'app/activity-log/activity-log.service';
import { ActivityLogDTO } from 'app/activity-log/activity-log.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';


@Component({
  selector: 'app-activity-log-add',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './activity-log-add.component.html'
})
export class ActivityLogAddComponent {

  activityLogService = inject(ActivityLogService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  addForm = new FormGroup({
    entityType: new FormControl(null),
    entityId: new FormControl(null),
    action: new FormControl(null),
    description: new FormControl(null, [Validators.maxLength(255)]),
    timestamp: new FormControl(null)
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@activityLog.create.success:Activity Log was created successfully.`
    };
    return messages[key];
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new ActivityLogDTO(this.addForm.value);
    this.activityLogService.createActivityLog(data)
        .subscribe({
          next: () => this.router.navigate(['/activityLogs'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
