import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { LeadService } from 'app/lead/lead.service';
import { LeadDTO } from 'app/lead/lead.model';


@Component({
  selector: 'app-lead-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './lead-list.component.html'})
export class LeadListComponent implements OnInit, OnDestroy {

  leadService = inject(LeadService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  leads?: LeadDTO[];
  navigationSubscription?: Subscription;

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@lead.delete.success:Lead was removed successfully.`,
      'lead.salesOpportunity.lead.referenced': $localize`:@@lead.salesOpportunity.lead.referenced:This entity is still referenced by Sales Opportunity ${details?.id} via field Lead.`
    };
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
    this.leadService.getAllLeads()
        .subscribe({
          next: (data) => this.leads = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  confirmDelete(id: number) {
    if (confirm(this.getMessage('confirm'))) {
      this.leadService.deleteLead(id)
          .subscribe({
            next: () => this.router.navigate(['/leads'], {
              state: {
                msgInfo: this.getMessage('deleted')
              }
            }),
            error: (error) => {
              if (error.error?.code === 'REFERENCED') {
                const messageParts = error.error.message.split(',');
                this.router.navigate(['/leads'], {
                  state: {
                    msgError: this.getMessage(messageParts[0], { id: messageParts[1] })
                  }
                });
                return;
              }
              this.errorHandler.handleServerError(error.error)
            }
          });
    }
  }

}
