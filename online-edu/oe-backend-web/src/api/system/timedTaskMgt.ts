import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/bsTimedTask/page',
    params,
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/bsTimedTask/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/bsTimedTask/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/bsTimedTask/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/bsTimedTask/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/bsTimedTask/delete',
    data,
  });
}
export function resumeJob(data: any) {
  return request.put<any>({
    url: '/admin/bsTimedTask/resumeJob',
    data,
  });
}
export function pauseJob(data: any) {
  return request.put<any>({
    url: '/admin/bsTimedTask/pauseJob',
    data,
  });
}
