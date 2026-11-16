export interface Service  { id: number; name: string; }
export interface Engineer { id: number; name: string; }
export interface Severity { id: number; name: string; slaMinutes: number; }

export interface Incident {
  id: number;
  title: string;
  serviceId: number;
  severityId: number;
  assigneeId: number | null;
  status: string;
  openedAt: string;
  resolvedAt: string | null;
}

// Same shape as the backend's IncidentRequest record
export interface IncidentRequest {
  title: string;
  serviceId: number | null;
  severityId: number | null;
  assigneeId: number | null;
  status: string;
}

export interface ReportRow {
  id: number;
  title: string;
  service: string;
  severity: string;
  assignee: string | null;
  status: string;
  openedAt: string;
  resolvedAt: string | null;
}

export interface ReportStats {
  total: number;
  unresolved: number;
  avgResolveMinutes: number | null;
}

export interface ReportResponse {
  stats: ReportStats;
  rows: ReportRow[];
}