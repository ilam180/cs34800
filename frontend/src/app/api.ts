import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Engineer, Incident, IncidentRequest, Service, Severity, ReportResponse } from './models';

@Injectable({ providedIn: 'root' })   // one shared instance for the whole app
export class Api {
  private http = inject(HttpClient);

  // Dropdowns
  getServices()   { return this.http.get<Service[]>('/api/services'); }
  getSeverities() { return this.http.get<Severity[]>('/api/severities'); }
  getEngineers()  { return this.http.get<Engineer[]>('/api/engineers'); }

  // Incidents
  getIncidents()  { return this.http.get<Incident[]>('/api/incidents'); }

  createIncident(req: IncidentRequest) {
    return this.http.post<Incident>('/api/incidents', req);
  }

  updateIncident(id: number, req: IncidentRequest) {
    return this.http.put<Incident>(`/api/incidents/${id}`, req);
  }
  deleteIncident(id: number) {
    return this.http.delete(`/api/incidents/${id}`);
  }

  // add HttpParams to the existing @angular/common/http import, and ReportResponse to the models import

  getReport(from: string, to: string, serviceId: number | null, severityId: number | null) {
    // Builds ?from=...&to=...&serviceId=... and skips any filter left as "All"
    let params = new HttpParams().set('from', from).set('to', to);
    if (serviceId !== null) params = params.set('serviceId', serviceId);
    if (severityId !== null) params = params.set('severityId', severityId);
    return this.http.get<ReportResponse>('/api/reports/incidents', { params });
  }
}