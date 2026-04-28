import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/bsTask/page',
    params,
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/bsTask/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/bsTask/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/bsTask/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/bsTask/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/bsTask/delete',
    data,
  });
}
export function listAll() {
  return request.get<any>({
    url: `/admin/bsTask/listAll`,
  });
}
export function resumeJob(data: any) {
  return request.put<any>({
    url: '/admin/bsTask/resumeJob',
    data,
  });
}
export function pauseJob(data: any) {
  return request.put<any>({
    url: '/admin/bsTask/pauseJob',
    data,
  });
}

export function run(data: any) {
  return request.put<any>({
    url: '/admin/bsTask/run',
    data,
  });
}
