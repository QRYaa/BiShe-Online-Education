import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/bsTimedTaskLog/page',
    params,
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/bsTimedTaskLog/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/bsTimedTaskLog/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/bsTimedTaskLog/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/bsTimedTaskLog/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/bsTimedTaskLog/delete',
    data,
  });
}
