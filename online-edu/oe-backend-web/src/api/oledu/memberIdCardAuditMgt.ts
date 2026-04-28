import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/oeMemberIdCardAudit/page',
    params,
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/oeMemberIdCardAudit/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/oeMemberIdCardAudit/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/oeMemberIdCardAudit/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/oeMemberIdCardAudit/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/oeMemberIdCardAudit/delete',
    data,
  });
}
export function passAudit(data: any) {
  return request.post<any>({
    url: '/admin/oeMemberIdCardAudit/passAudit',
    data,
  });
}
export function rejectAudit(data: any) {
  return request.post<any>({
    url: '/admin/oeMemberIdCardAudit/rejectAudit',
    data,
  });
}
