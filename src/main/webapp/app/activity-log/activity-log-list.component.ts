import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { ActivityLogService } from 'app/activity-log/activity-log.service';
import { ActivityLogDTO } from 'app/activity-log/activity-log.model';


@Component({
  selector: 'app-activity-log-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './activity-log-list.component.html'})
export class ActivityLogListComponent implements OnInit, OnDestroy {

  activityLogService = inject(ActivityLogService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  activityLogs?: ActivityLogDTO[];
  navigationSubscription?: Subscription;

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@activityLog.delete.success:Activity Log was removed successfully.`    };
    return messages[key];
  }

  ngOnInit() {
    this.loadData();
    this.navigationSubscription = this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.loadData();
      }
    });
  }

  ngOnDestroy() {
    this.navigationSubscription!.unsubscribe();
  }
  
  loadData() {
    this.activityLogService.getAllActivityLogs()
        .subscribe({
          next: (data) => this.activityLogs = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  confirmDelete(id: number) {
    if (confirm(this.getMessage('confirm'))) {
      this.activityLogService.deleteActivityLog(id)
          .subscribe({
            next: () => this.router.navigate(['/activityLogs'], {
              state: {
                msgInfo: this.getMessage('deleted')
              }
            }),
            error: (error) => this.errorHandler.handleServerError(error.error)
          });
    }
  }

}
