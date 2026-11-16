import { Component, OnInit, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Api } from '../api';
import { ReportResponse, Service, Severity } from '../models';

@Component({
  selector: 'app-report',
  imports: [DatePipe, FormsModule],
  templateUrl: './report.html',
})
export class Report implements OnInit {
  private api = inject(Api);

  // Dropdown data
  services   = signal<Service[]>([]);
  severities = signal<Severity[]>([]);

  // The report result: null until the user clicks Run
  result = signal<ReportResponse | null>(null);

  // What the user picked (dates are 'YYYY-MM-DD' strings)
  filter = {
    from: this.daysAgo(30),
    to: this.daysAgo(0),
    serviceId: null as number | null,
    severityId: null as number | null,
  };

  ngOnInit() {
    this.api.getServices().subscribe(data => this.services.set(data));
    this.api.getSeverities().subscribe(data => this.severities.set(data));
  }

  run() {
    this.api.getReport(this.filter.from, this.filter.to, this.filter.serviceId, this.filter.severityId)
      .subscribe(response => this.result.set(response));
  }

  // Returns the date N days ago as 'YYYY-MM-DD'
  private daysAgo(n: number): string {
    const d = new Date();
    d.setDate(d.getDate() - n);
    return d.toISOString().slice(0, 10);
  }
}